package br.com.project.samuraicars.DTO.user;

import br.com.project.samuraicars.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserResponseBody(
        Long id,
        String name,
        String email,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
    public UserResponseBody(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}
