package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehiclePhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final VehicleService vehicleService;
    private final UserService userService;

    @Transactional
    public void savePhotos(List<MultipartFile> photos, Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        if (!userService.isUserOwnerOfResource(userDetails, vehicle)) {
            throw new BadRequestException("User can't add photos to this vehicle");
        }
        AtomicInteger photoNumber = new AtomicInteger(1);
        photos.forEach((photo) -> {
            try {
                byte[] photosBytes = photo.getBytes();
                SerialBlob serialBlob = new SerialBlob(photosBytes);
                VehiclePhoto vehiclePhoto = new VehiclePhoto(vehicle.getName() + photoNumber, serialBlob, vehicle);
                photoNumber.getAndIncrement();
                photoRepository.save(vehiclePhoto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public byte[] findImageById(Long id) {
        Blob image = findById(id).getImage();
        try {
            return image.getBytes(1, (int) image.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPhotosUrlByVehicleId(List<Long> photoIds, UriComponentsBuilder uriComponentsBuilder) {
        String uriString = uriComponentsBuilder.toUriString();
        return photoIds.stream().map(id -> uriString + "/photos/" + id).toList();
    }

    @Transactional
    public void delete(Long id, UserDetails userDetails) {
        VehiclePhoto vehiclePhoto = findById(id);
        if (userService.isUserOwnerOfResource(userDetails, vehiclePhoto.getVehicle()) || userService.isUserAdmin(userDetails)) {
            photoRepository.delete(vehiclePhoto);
        }
    }

    @Transactional
    public void replace(Long photoId, MultipartFile photo) {
        try {
            byte[] photoBytes = photo.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            VehiclePhoto vehiclePhoto = findById(photoId);
            vehiclePhoto.setImage(photoBlob);
            photoRepository.save(vehiclePhoto);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private VehiclePhoto findById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new BadRequestException("Bad Request image not found"));
    }
}
