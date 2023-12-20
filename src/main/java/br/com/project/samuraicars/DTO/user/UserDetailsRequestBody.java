package br.com.project.samuraicars.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDetailsRequestBody(
        @JsonProperty("user_id")
        Long userId,
        String name,
        String authorities
) {
}
