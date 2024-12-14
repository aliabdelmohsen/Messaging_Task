package com.thiqa.messaging.service;

import com.thiqa.messaging.enums.RoleEnum;
import com.thiqa.messaging.exception.BusinessException;
import com.thiqa.messaging.model.User;
import com.thiqa.messaging.repository.UserRepository;
import com.thiqa.messaging.request.auth.AuthenticationRequest;
import com.thiqa.messaging.request.auth.SignUpRequest;
import com.thiqa.messaging.response.auth.AuthenticationResponse;
import com.thiqa.messaging.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthenticationService.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Test signup(SignUpRequest); given JwtService generateToken(UserDetails) return 'ABC123'; then return Token is 'ABC123'")
    void testSignup_givenJwtServiceGenerateTokenReturnAbc123_thenReturnTokenIsAbc123() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(RoleEnum.USER);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");

        SignUpRequest request = new SignUpRequest();
        request.setEmail("jane.doe@example.org");
        request.setFirstname("Jane");
        request.setLastname("Doe");
        request.setPassword("iloveyou");

        AuthenticationResponse actualSignupResult = authenticationService.signup(request);

        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(userRepository).save(isA(User.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("ABC123", actualSignupResult.getToken());
    }


    @Test
    @DisplayName("Test signup(SignUpRequest); then throw BusinessException")
    void testSignup_thenThrowBusinessException() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(RoleEnum.USER);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenThrow(new BusinessException("An error occurred"));

        SignUpRequest request = new SignUpRequest();
        request.setEmail("jane.doe@example.org");
        request.setFirstname("Jane");
        request.setLastname("Doe");
        request.setPassword("iloveyou");

        assertThrows(BusinessException.class, () -> authenticationService.signup(request));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(userRepository).save(isA(User.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
    }

    @Test
    @DisplayName("Test authenticate(AuthenticationRequest); given User() Email is 'jane.doe@example.org'; then return Token is 'ABC123'")
    void testAuthenticate_givenUserEmailIsJaneDoeExampleOrg_thenReturnTokenIsAbc123() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(RoleEnum.USER);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("jane.doe@example.org");
        request.setPassword("iloveyou");

        AuthenticationResponse actualAuthenticateResult = authenticationService.authenticate(request);

        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
        assertEquals("ABC123", actualAuthenticateResult.getToken());
    }


    @Test
    @DisplayName("Test authenticate(AuthenticationRequest); then throw BusinessException")
    void testAuthenticate_thenThrowBusinessException() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(RoleEnum.USER);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenThrow(new BusinessException("An error occurred"));

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("jane.doe@example.org");
        request.setPassword("iloveyou");

        assertThrows(BusinessException.class, () -> authenticationService.authenticate(request));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
    }
}
