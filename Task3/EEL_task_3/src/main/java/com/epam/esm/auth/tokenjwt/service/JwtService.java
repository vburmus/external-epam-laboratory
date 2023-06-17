package com.epam.esm.auth.tokenjwt.service;

import com.epam.esm.auth.tokenjwt.TokenType;
import com.epam.esm.exceptionhandler.exceptions.nonrest.CacheError;
import com.epam.esm.exceptionhandler.exceptions.nonrest.IncorrectTokenTypeException;
import com.epam.esm.utils.config.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {


    private final KeyUtils keyUtils;
    private final CacheManager cacheManager;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public long extractId(String token) {
        return extractClaim(token, claims -> claims.get(ID, Long.class));
    }

    public String extractType(String token) {
        return extractClaim(token, claims -> claims.get(TYPE, String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(keyUtils.getAccessTokenPublicKey()).build().parseClaimsJws(token).getBody();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUsername(token);

        return email.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBanned(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public boolean isTokenBanned(String token) {

        final long id = extractId(token);

        List<?> actualizedJwt = actualizeCache(id);
        return actualizedJwt.stream().anyMatch(o -> o.equals(token));

    }

    private List<?> actualizeCache(long id) {
        Cache blackList = cacheManager.getCache(BLACK_LIST);
        if (blackList == null) {
            throw new CacheError(CACHE_NOT_FOUND);
        }
        var cache = blackList.get(id);
        if (cache != null) {
            var bannedJwt = cache.get();
            if (bannedJwt instanceof List) {
                List<?> actualizedJwt = ((List<?>) bannedJwt).stream().filter(jwt -> !isTokenExpired((String) jwt)).toList();
                blackList.put(id, actualizedJwt);
                return actualizedJwt;
            }
        }
        return new ArrayList<>();
    }

    public void invalidateTokenIfExist(long userId, TokenType tokenType) throws IncorrectTokenTypeException {

        Cache cache;
        if (tokenType == TokenType.ACCESS_TOKEN) {
            cache = cacheManager.getCache(ACCESS_TOKENS);
        } else if (tokenType == TokenType.REFRESH_TOKEN) {
            cache = cacheManager.getCache(REFRESH_TOKENS);
        } else {
            throw new IncorrectTokenTypeException();
        }

        if (cache == null) {
            throw new CacheError(CACHE_NOT_FOUND);
        }

        var oldToken = cache.get(userId);

        if (oldToken != null) {
            String cachedJwt = (String) oldToken.get();
            addTokenToBlackList(userId, cachedJwt);
        }
    }

    private void addTokenToBlackList(long userId, String cachedJwt) {

        Cache blackList = cacheManager.getCache(BLACK_LIST);

        if (blackList == null) {
            throw new CacheError(CACHE_NOT_FOUND);
        }
        var cachedList = blackList.get(userId);

        List<String> jwtList;
        if (cachedList == null) {
            jwtList = new ArrayList<>();
        } else {
            jwtList = (List<String>) cachedList.get();
        }
        jwtList.add(cachedJwt);
        blackList.put(userId, jwtList);
    }

    public boolean isRefreshToken(String token) {
        return Objects.equals(extractType(token), TokenType.REFRESH_TOKEN.name());
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_BEARER_TOKEN)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
