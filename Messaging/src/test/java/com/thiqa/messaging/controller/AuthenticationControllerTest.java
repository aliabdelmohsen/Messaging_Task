package com.thiqa.messaging.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiqa.messaging.request.auth.AuthenticationRequest;
import com.thiqa.messaging.request.auth.SignUpRequest;
import com.thiqa.messaging.response.auth.AuthenticationResponse;
import com.thiqa.messaging.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthenticationController.class})
@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Test signup(SignUpRequest)")
    void testSignup() throws Exception {
        AuthenticationResponse buildResult = AuthenticationResponse.builder().token("ABC123").build();
        when(authenticationService.signup(Mockito.<SignUpRequest>any())).thenReturn(buildResult);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setFirstname("Jane");
        signUpRequest.setLastname("Doe");
        signUpRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(signUpRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123\"}"));
    }


    @Test
    @DisplayName("Test authenticate(AuthenticationRequest)")
    void testAuthenticate() throws Exception {
        AuthenticationResponse buildResult = AuthenticationResponse.builder().token("ABC123").build();
        when(authenticationService.authenticate(Mockito.<AuthenticationRequest>any())).thenReturn(buildResult);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("jane.doe@example.org");
        authenticationRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123\"}"));
    }
}
