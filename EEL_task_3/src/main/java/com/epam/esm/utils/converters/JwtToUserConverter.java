package com.epam.esm.utils.converters;

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

import static com.epam.esm.utils.Constants.*;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = User.builder()
                .id(jwt.getClaim(ID))
                .role(Role.valueOf(jwt.getClaim(ROLE)))
                .name(jwt.getClaim(NAME))
                .surname(jwt.getClaim(SURNAME))
                .email(jwt.getSubject())
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
}