package com.epam.esm.auth.tokenjwt;

import com.epam.esm.auth.tokenjwt.model.TokenDTO;
import com.epam.esm.auth.tokenjwt.service.JwtService;
import com.epam.esm.exceptionhandler.exceptions.nonrest.CacheError;
import com.epam.esm.exceptionhandler.exceptions.nonrest.IncorrectTokenTypeException;
import com.epam.esm.exceptionhandler.exceptions.rest.WrongAuthenticationInstanceException;
import com.epam.esm.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.epam.esm.utils.Constants.*;
import static com.epam.esm.utils.config.ApplicationConfig.ACCESS_TOKENS;
import static com.epam.esm.utils.config.ApplicationConfig.REFRESH_TOKENS;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenGenerator {
    private final JwtEncoder jwtAccessTokenEncoder;
    private final JwtEncoder jwtRefreshTokenEncoder;
    private final CacheManager cacheManager;
    private final JwtService jwtService;

    private static JwtClaimsSet makeJwtClaimSet(User user, TokenType tokenType) {
        Instant now = Instant.now();
        var jwtClaimsSet = JwtClaimsSet.builder()
                .claim(ID, user.getId())
                .subject(user.getEmail())
                .claim(NAME, user.getName())
                .claim(SURNAME, user.getSurname())
                .claim(ROLE, user.getRole())
                .issuer(EPAM)
                .claim(PROVIDER, user.getProvider())
                .claim(TYPE, tokenType)
                .issuedAt(now);
        if (tokenType == TokenType.ACCESS_TOKEN) {
            return jwtClaimsSet.expiresAt(now.plus(60, ChronoUnit.SECONDS)).build();
        } else if (tokenType == TokenType.REFRESH_TOKEN) {
            return jwtClaimsSet.expiresAt(now.plus(7, ChronoUnit.DAYS)).build();
        }
        throw new IncorrectTokenTypeException();
    }

    public String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        try {
            jwtService.invalidateTokenIfExist(user.getId(), TokenType.ACCESS_TOKEN);
        } catch (IncorrectTokenTypeException e) {
            log.error(e.getMessage());
        }

        JwtClaimsSet jwtClaimSet = makeJwtClaimSet(user, TokenType.ACCESS_TOKEN);

        return encodeAndPutJwt(user, jwtClaimSet, jwtAccessTokenEncoder, ACCESS_TOKENS);
    }

    private String encodeAndPutJwt(User user, JwtClaimsSet jwtClaimSet, JwtEncoder jwtAccessTokenEncoder, String accessTokens) {
        String jwt = jwtAccessTokenEncoder.encode(JwtEncoderParameters.from(jwtClaimSet)).getTokenValue();
        var cache = cacheManager.getCache(accessTokens);
        if (cache == null) {
            throw new CacheError(CACHE_NOT_FOUND);
        }
        cache.put(user.getId(), jwt);
        return jwt;
    }

    public String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        try {
            jwtService.invalidateTokenIfExist(user.getId(), TokenType.REFRESH_TOKEN);
        } catch (IncorrectTokenTypeException e) {
            log.error(e.getMessage());
        }
        JwtClaimsSet jwtClaimSet = makeJwtClaimSet(user, TokenType.REFRESH_TOKEN);

        return encodeAndPutJwt(user, jwtClaimSet, jwtRefreshTokenEncoder, REFRESH_TOKENS);
    }

    public TokenDTO createToken(Authentication authentication) {

        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new WrongAuthenticationInstanceException(OTHER_INSTANCE_DETECTED);
        }

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));
        tokenDTO.setRefreshToken(createRefreshToken(authentication));

        return tokenDTO;
    }
}
