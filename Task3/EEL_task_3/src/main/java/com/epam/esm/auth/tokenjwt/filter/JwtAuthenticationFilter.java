package com.epam.esm.auth.tokenjwt.filter;

import com.epam.esm.auth.tokenjwt.service.JwtService;
import com.epam.esm.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String jwt = jwtService.resolveToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userEmail);
            if (isEndpointAllowed(request) || (jwtService.isTokenValid(jwt, userDetails) && !jwtService.isRefreshToken(jwt))) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean isEndpointAllowed(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals("/auth/refresh-token");
    }
}
