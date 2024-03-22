package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;

public class VehicleResponseBodyCreator {
    public static VehicleResponseBody createValidVehicleResponseBody() {
        return VehicleResponseBody.builder()
                .id(1L)
                .userId(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("Fastback de 2 portas")
                .brand("Volkswagen")
                .price(5000.00)
                .build();
    }

    public static VehicleResponseBody createUpdatedVehicleResponseBody() {
        return VehicleResponseBody.builder()
                .id(1L)
                .userId(1L)
                .name("Civic")
                .model("EX")
                .year(2022L)
                .vehicleType("sedan")
                .brand("Honda")
                .price(25000.00)
                .build();
    }
}
