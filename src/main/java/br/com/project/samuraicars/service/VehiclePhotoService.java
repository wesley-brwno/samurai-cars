package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehiclePhotoRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class VehiclePhotoService {
    private final VehiclePhotoRepository photoRepository;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    public void save(List<MultipartFile> photos, Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            AtomicInteger i = new AtomicInteger();
            photos.stream()
                    .limit(5 - checkImageQuantityByVehicle(vehicle))
                    .forEach((photo) -> {
                        try {
                            byte[] photoBytes = photo.getBytes();
                            Blob photoBlob = new SerialBlob(photoBytes);
                            VehiclePhoto vehiclePhoto = new VehiclePhoto("image-" + i.getAndIncrement(), photoBlob, vehicle);
                            System.err.println(vehiclePhoto);
                            photoRepository.save(vehiclePhoto);

                        } catch (SQLException | IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    public byte[] findById(Long id) {
        VehiclePhoto vehiclePhoto = photoRepository.findById(id).orElseThrow(() -> new BadRequestException("Bad Request image not found"));
        Blob image = vehiclePhoto.getImage();
        try {
            return image.getBytes(1, (int) image.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getImagesPathByVehicleId(Long vehicleId) {
        List<Long> photosId = vehicleRepository.findAllByVehicleId(vehicleId);
        return photosId.stream().map(id -> "/photos/" + id).toList();
    }
    public void delete(Long id, UserDetails userDetails) {
        VehiclePhoto vehiclePhoto = photoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("The photo was not found"));
        if (userService.isUserOwnerOfResource(userDetails, vehiclePhoto.getVehicle())
                || userService.isUserAdmin(userDetails)) {
            photoRepository.delete(vehiclePhoto);
        }
    }
    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new BadRequestException("Bad Request"));
    }

    //todo: isolate this validation
    private int checkImageQuantityByVehicle(Vehicle vehicle) {
        if (vehicle.getPhotos().size() <= 5) {
            return vehicle.getPhotos().size();
        }
        throw new BadRequestException("Cannot insert more than five photos");
    }


}
