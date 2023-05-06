package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.AuthenticationResponse;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.tokenjwt.JwtService;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public static final String ALREADY_REGISTERED = "Such user has already registered";
    public static final String USER_DOESNT_EXIST = "Such user doesnt exist";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ObjectAlreadyExistsException(ALREADY_REGISTERED);

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOESNT_EXIST));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
