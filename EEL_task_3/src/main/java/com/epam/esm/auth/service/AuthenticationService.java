package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.tokenjwt.TokenGenerator;
import com.epam.esm.auth.tokenjwt.model.TokenDTO;
import com.epam.esm.auth.tokenjwt.service.JwtService;
import com.epam.esm.exceptionhandler.exceptions.rest.EmailNotFoundException;
import com.epam.esm.exceptionhandler.exceptions.rest.InvalidTokenException;
import com.epam.esm.exceptionhandler.exceptions.rest.ObjectAlreadyExistsException;
import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.user.service.CustomUserDetailsService;
import com.epam.esm.utils.AwsUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Transactional
    public TokenDTO register(RegisterRequest request, MultipartFile image) {
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

        if (image != null) {
            user.setImageURL(AwsUtils.loadImage(image, "/user"));
        } else {
            user.setImageURL(DEFAULT_PROFILE_IMG);
        }
        userRepository.save(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user,
                        request.getPassword());
        return tokenGenerator.createToken(usernamePasswordAuthenticationToken);

    }

    public TokenDTO authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        User user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(USER_DOESNT_EXIST));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user,
                        request.getPassword());

        return tokenGenerator.createToken(usernamePasswordAuthenticationToken);
    }

    public String refreshToken(String jwt) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new EmailNotFoundException(MISSING_USER_EMAIL);
        }
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userEmail);
        if (!jwtService.isTokenValid(jwt, userDetails)) {
            throw new InvalidTokenException(TOKEN_IS_INVALID);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                        userDetails.getAuthorities());
        return tokenGenerator.createAccessToken(usernamePasswordAuthenticationToken);
    }
}