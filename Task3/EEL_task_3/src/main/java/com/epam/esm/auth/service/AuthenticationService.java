package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.tokenjwt.TokenDTO;
import com.epam.esm.auth.tokenjwt.TokenGenerator;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public TokenDTO register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ObjectAlreadyExistsException(ALREADY_REGISTERED);

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .number(request.getNumber())
                .provider(LOCAL_PROVIDER)
                .build();
        userRepository.save(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user
                , request.getPassword());
        return tokenGenerator.createToken(usernamePasswordAuthenticationToken);

    }

    public TokenDTO authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOESNT_EXIST));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                request.getPassword());

        return tokenGenerator.createToken(usernamePasswordAuthenticationToken);
    }
}
