package br.com.project.samuraicars.DTO.vehicle;


import br.com.project.samuraicars.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record VehicleGetResponseBody(
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        Long id,
        String name,
        String model,
        Long year,
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("vehicle_type")
        String vehicleType,
        String brand,
        Double price
) {
        public VehicleGetResponseBody(@JsonProperty("created_at")
                                      LocalDateTime createdAt, Long id, String name, String model, Long year, @JsonProperty("user_id")
                                      Long userId, @JsonProperty("vehicle_type")
                                      String vehicleType, String brand, Double price) {
                this.createdAt = createdAt;
                this.id = id;
                this.name = name;
                this.model = model;
                this.year = year;
                this.userId = userId;
                this.vehicleType = vehicleType;
                this.brand = brand;
                this.price = price;
        }

        public VehicleGetResponseBody(Vehicle vehicle) {
                this(vehicle.getCreatedAt(), vehicle.getId(), vehicle.getName(), vehicle.getModel(), vehicle.getYear(),
                        vehicle.getUser().getId(), vehicle.getVehicleType(), vehicle.getBrand(), vehicle.getPrice());
        }


}
