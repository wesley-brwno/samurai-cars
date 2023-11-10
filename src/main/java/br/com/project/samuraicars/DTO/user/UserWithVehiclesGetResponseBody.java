package br.com.project.samuraicars.DTO.user;

import br.com.project.samuraicars.DTO.vehicle.VehicleGetResponseBody;

import java.util.List;

public record UserWithVehiclesGetResponseBody(
        Long id,
        String name,
        List<VehicleGetResponseBody> vehicles
) {
}
