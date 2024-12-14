package com.thiqa.messaging.service;

import com.thiqa.messaging.enums.RoleEnum;
import com.thiqa.messaging.exception.BusinessException;
import com.thiqa.messaging.model.User;
import com.thiqa.messaging.repository.UserRepository;
import com.thiqa.messaging.request.auth.AuthenticationRequest;
import com.thiqa.messaging.request.auth.SignUpRequest;
import com.thiqa.messaging.response.auth.AuthenticationResponse;
import com.thiqa.messaging.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    public AuthenticationResponse signup(final SignUpRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("USER.NOT_FOUND", null, Locale.US)));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
