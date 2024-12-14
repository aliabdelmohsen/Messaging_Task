package com.thiqa.messaging.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "email field is required")
    private String email;
    @NotBlank(message = "password field is required")
    private String password;
}
