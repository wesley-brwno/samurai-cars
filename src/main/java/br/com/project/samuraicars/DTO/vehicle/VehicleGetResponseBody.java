package br.com.project.samuraicars.DTO.vehicle;


import br.com.project.samuraicars.model.Vehicle;
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
        public VehicleGetResponseBody(@JsonProperty("created_at") LocalDateTime createdAt, Long id, String name,
                                      String model, Long year, @JsonProperty("user_id") Long userId) {
                this.createdAt = createdAt;
                this.id = id;
                this.name = name;
                this.model = model;
                this.year = year;
                this.userId = userId;
        }

        public VehicleGetResponseBody(Vehicle vehicle) {
                this(vehicle.getCreatedAt(), vehicle.getId(), vehicle.getName(), vehicle.getModel(), vehicle.getYear(), vehicle.getUser().getId());
        }


}
