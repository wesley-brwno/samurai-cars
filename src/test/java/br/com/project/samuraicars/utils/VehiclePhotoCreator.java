package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import org.mockito.Mockito;

import java.sql.Blob;

public class VehiclePhotoCreator {
    public static VehiclePhoto createValidVehiclePhoto() {
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        Blob mockBlob = Mockito.mock(Blob.class);
        return new VehiclePhoto("photo-1", mockBlob, vehicle);
    }
}
