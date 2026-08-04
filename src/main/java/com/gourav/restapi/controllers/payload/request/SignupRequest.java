package com.gourav.restapi.controllers.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be 3–20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email(message = "Must be a valid email address")
    private String email;

    private Set<String> roles;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 40, message = "Password must be 8–40 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
        message = "Password must contain at least one uppercase letter, one digit, and one special character"
    )
    private String password;
}
