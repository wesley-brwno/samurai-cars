package br.com.project.samuraicars.DTO.contactMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ContactMessageResponseBody(
        String name,
        String lastName,
        String phone,
        String email,
        String message,
        Long id,
        @JsonProperty("vehicle_id")
        Long vehicleId,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("is_read")
        boolean isRead
) {
}