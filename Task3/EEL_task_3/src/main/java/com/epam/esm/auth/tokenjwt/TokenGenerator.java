package com.epam.esm.auth.tokenjwt;

import com.epam.esm.exceptionhandler.exceptions.WrongAuthenticationInstance;
import com.epam.esm.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtEncoder jwtAccessTokenEncoder;
    private final JwtEncoder jwtRefreshTokenEncoder;

    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimSet = JwtClaimsSet.builder()
                .issuer("epam")
                .issuedAt(now)
                .expiresAt(now.plus(60, ChronoUnit.MINUTES))
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("provider", user.getProvider())
                .build();

        return jwtAccessTokenEncoder.encode(JwtEncoderParameters.from(jwtClaimSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimSet = JwtClaimsSet.builder()
                .claim("id", user.getId())
                .subject(user.getEmail())
                .issuer("epam")
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiresAt(now.plus(60, ChronoUnit.MINUTES))
                .claim("provider", user.getProvider())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtClaimSet)).getTokenValue();
    }

    public TokenDTO createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new WrongAuthenticationInstance("Expected User instance, but other instance detected.");
        }

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));


        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }

        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }
}
