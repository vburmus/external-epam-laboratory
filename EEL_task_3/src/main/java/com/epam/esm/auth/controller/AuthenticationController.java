package com.epam.esm.auth.controller;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.AuthenticationResponse;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.service.AuthenticationService;
import com.epam.esm.exceptionhandler.exceptions.rest.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

import static com.epam.esm.utils.Constants.AUTHENTICATION_BEARER_TOKEN;
import static com.epam.esm.utils.Constants.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register", consumes = "multipart/form-data")
    public ResponseEntity<AuthenticationResponse> register(@ModelAttribute RegisterRequest request,
                                                           @RequestParam @Nullable MultipartFile image) {
        return ResponseEntity.ok(new AuthenticationResponse(authenticationService.register(request, image)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse =
                new AuthenticationResponse(authenticationService.authenticate(request));
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String jwt;
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_BEARER_TOKEN)) {
            jwt = bearerToken.substring(7);
        } else {
            throw new InvalidTokenException("Refresh request should contain refresh token.");
        }
        String token = authenticationService.refreshToken(jwt);
        response.setHeader(AUTHORIZATION_HEADER,
                AUTHENTICATION_BEARER_TOKEN + token);
        return ResponseEntity.noContent().build();
    }
}