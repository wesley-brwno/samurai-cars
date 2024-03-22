package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;

import java.util.List;

public class UserCreator {
    public static User createValidUser() {
        Vehicle vehicle = VehicleCreator.createValidVehicle();

        return User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("ADMIN")
                .build();
    }
}
