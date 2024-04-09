package br.com.project.samuraicars.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseBody(
        @JsonProperty("user_id")
        Long userId,
        String name
) {
}
