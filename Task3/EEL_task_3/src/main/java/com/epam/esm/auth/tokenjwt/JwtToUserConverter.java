package com.epam.esm.auth.tokenjwt;

import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = User.builder()
                .id(jwt.getClaim("id"))
                .role(Role.valueOf(jwt.getClaim("role")))
                .name(jwt.getClaim("name"))
                .surname(jwt.getClaim("surname"))
                .email(jwt.getSubject())
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
}
