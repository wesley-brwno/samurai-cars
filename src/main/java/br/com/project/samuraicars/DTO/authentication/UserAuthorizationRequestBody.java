package br.com.project.samuraicars.DTO.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAuthorizationRequestBody(
        @NotBlank()
        @Email()
        String email,
        @NotBlank()
        @Size(min = 5, max = 100)
        String password
) {
}
