package br.com.project.samuraicars.DTO.vehicle;

import java.util.List;

public record VehicleDetailsGetResponseBody(
        VehicleGetResponseBody vehicle,
        List<String> pictures
) {
}
