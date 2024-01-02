package br.com.project.samuraicars.DTO.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record VehiclePutRequestBody(
        @NotNull()
        @Positive()
        Long id,
        @NotBlank()
        @Size(min = 2, max = 100)
        String name,
        @NotBlank()
        @Size(min = 2, max = 100)
        String model,
        @NotNull()
        @Digits(integer = 4, fraction = 0)
        @Positive()
        Long year,
        @JsonProperty("vehicle_type")
        @NotBlank()
        @Size(min = 2, max = 100)
        String vehicleType,
        @NotBlank()
        @Size(min = 2, max = 100)
        String brand,
        @NotNull()
        @Positive()
        Double price
) {
}
