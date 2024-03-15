package br.com.project.samuraicars.DTO.vehicle;


import br.com.project.samuraicars.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record VehicleResponseBody(
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
        public VehicleResponseBody(@JsonProperty("created_at")
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

        public VehicleResponseBody(Vehicle vehicle) {
                this(vehicle.getCreatedAt(), vehicle.getId(), vehicle.getName(), vehicle.getModel(), vehicle.getYear(),
                        vehicle.getUser().getId(), vehicle.getVehicleType(), vehicle.getBrand(), vehicle.getPrice());
        }


}
