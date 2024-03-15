package br.com.project.samuraicars.DTO.vehicle;

import java.util.List;

public record VehicleWithPhotosResponseBody(
        VehicleResponseBody vehicle,
        List<String> pictures
) {
}
