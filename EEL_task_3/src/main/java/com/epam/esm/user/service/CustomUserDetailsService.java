package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exceptions.rest.UserCreationFailureException;
import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.epam.esm.utils.Constants.EMAIL;
import static com.epam.esm.utils.Constants.NAME;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User createUserFromAttributes(Map<String, Object> attributes, String provider) {
        String email = (String) attributes.get(EMAIL);

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            String[] fullName = attributes.get(NAME).toString().split(" ");
            User newUser = User.builder()
                    .name(fullName[0])
                    .surname(fullName[1])
                    .email(email)
                    .role(Role.USER)
                    .provider(provider)
                    .build();
            userRepository.save(newUser);
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new UserCreationFailureException("Failed to create" +
                " or retrieve user"));
    }
}