package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;

import java.util.List;

public class VehicleWithPhotosResponseBodyCreator {
    public static VehicleWithPhotosResponseBody createValidVehicleWithPhotosResponseBody() {
        VehicleResponseBody vehicleResponseBody = VehicleResponseBody.builder()
                .id(1L)
                .userId(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("Fastback de 2 portas")
                .brand("Volkswagen")
                .price(5000.00)
                .build();

        List<String> photos = List.of("null/photos/1");

        return new VehicleWithPhotosResponseBody(vehicleResponseBody, photos);
    }
}
