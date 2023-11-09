package br.com.project.samuraicars.DTO.vehicle;

public record VehiclePutRequestBody(
        Long id,
        String name,
        String model,
        Long year
) {
}
