package br.com.project.samuraicars.DTO.vehicle;

public record VehiclePostRequestBody(
        String name,
        String model,
        Long year
) {
}
