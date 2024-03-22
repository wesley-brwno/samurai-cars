package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;

import java.time.LocalDateTime;

public class VehiclePostRequestBodyCreator {
    public static VehiclePostRequestBody createValidVehiclePostRequestBody() {
        return new VehiclePostRequestBody(
                "Fusca",
                "1300 GL",
                1991L,
                "Fastback de 2 portas",
                "Volkswagen",
                5000.00
        );
    }
}
