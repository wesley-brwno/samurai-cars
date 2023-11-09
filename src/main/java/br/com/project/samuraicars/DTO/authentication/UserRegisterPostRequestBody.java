package br.com.project.samuraicars.DTO.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterPostRequestBody(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password
) {
}
