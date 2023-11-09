package br.com.project.samuraicars.DTO.vehicle;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record VehicleGetResponseBody(
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        Long id,
        String name,
        String model,
        Long year,
        @JsonProperty("user_id")
        Long userId
) {
}
