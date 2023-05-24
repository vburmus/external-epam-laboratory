package com.epam.esm.oauth.service;

import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository; // Your custom UserRepository
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        customUserDetailsService.createUserFromAttributes(attributes, userRequest.getClientRegistration().getRegistrationId());
        Set<GrantedAuthority> authoritySet = new HashSet<>(oidcUser.getAuthorities());
        authoritySet.add(new SimpleGrantedAuthority("USER"));
        OidcIdToken token = userRequest.getIdToken();
        return new DefaultOidcUser(authoritySet, token, "sub");
    }
}
