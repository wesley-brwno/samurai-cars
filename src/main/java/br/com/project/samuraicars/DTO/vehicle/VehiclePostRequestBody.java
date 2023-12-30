package br.com.project.samuraicars.DTO.vehicle;

import jakarta.validation.constraints.*;

public record VehiclePostRequestBody(
        @NotBlank()
        @Size(min = 2, max = 100)
        String name,
        @NotBlank()
        @Size(min = 2, max = 100)
        String model,
        @NotNull()
        @Digits(integer = 4, fraction = 0)
        @Positive()
        Long year
) {
}
