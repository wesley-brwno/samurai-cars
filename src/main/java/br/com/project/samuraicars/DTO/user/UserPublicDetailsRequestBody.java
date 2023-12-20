package br.com.project.samuraicars.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserPublicDetailsRequestBody(
        Long id,
        String name,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}
