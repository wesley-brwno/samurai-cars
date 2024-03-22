package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;

import java.util.List;

public class VehicleCreator {
    public static Vehicle createValidVehicle() {
        User user = User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("USER")
                .build();

        VehiclePhoto vehiclePhoto = new VehiclePhoto("vehiclePhoto", null, null);
        vehiclePhoto.setId(1L);

        return Vehicle.builder()
                .id(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("Fastback de 2 portas")
                .brand("Volkswagen")
                .price(5000.00)
                .user(user)
                .photos(List.of(vehiclePhoto))
                .build();
    }
}
