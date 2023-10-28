package br.com.project.samuraicars.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPostRequestBody(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password
) {
}
