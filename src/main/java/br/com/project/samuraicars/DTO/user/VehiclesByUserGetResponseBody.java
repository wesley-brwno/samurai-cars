package br.com.project.samuraicars.DTO.user;

import br.com.project.samuraicars.DTO.vehicle.VehicleDetailsGetResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record VehiclesByUserGetResponseBody(
        @JsonProperty("user_id")
        Long userId,
        String name,
        List<VehicleDetailsGetResponseBody> vehicles
) {
}
