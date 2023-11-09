package br.com.project.samuraicars.DTO.authentication;

public record UserAuthorizationRequestBody(
        String email,
        String password
) {
}
