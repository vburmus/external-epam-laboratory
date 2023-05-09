package com.epam.esm.utils.config;

import com.epam.esm.auth.tokenjwt.JwtAuthenticationFilter;
import com.epam.esm.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/",
                        "/auth/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/error")
                .permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/certificate",
                        "/certificate/*",
                        "/certificate/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/**",
                        "/*"
                )
                .hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST,
                        "/user/create-order"
                )
                .hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers(
                        "/**"
                )
                .hasAuthority(Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l -> l.logoutSuccessUrl("/").permitAll())
                .exceptionHandling()
                .and()
                .oauth2Login();


        return http.build();

    }
}
