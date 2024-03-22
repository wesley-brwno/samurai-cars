package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;

public class VehiclePutRequestBodyCreator {
    public static VehiclePutRequestBody createValidVehiclePutRequestBody() {
        return new VehiclePutRequestBody(
                1L,
                "Civic",
                "EX",
                2022L,
                "sedan",
                "Honda",
                25000.00
        );
    }
}
