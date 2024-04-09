package br.com.project.samuraicars.DTO.vehicle;


import br.com.project.samuraicars.DTO.user.UserResponseBody;
import br.com.project.samuraicars.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VehicleResponseBody(
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        Long id,
        String name,
        String model,
        Long year,
        UserResponseBody owner,
        @JsonProperty("vehicle_type")
        String vehicleType,
        String brand,
        Double price
) {
}
