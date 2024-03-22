package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehiclePhotoRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class VehiclePhotoServiceImpl implements VehiclePhotoService {
    private final VehiclePhotoRepository photoRepository;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void save(List<MultipartFile> photos, Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (!userService.isUserOwnerOfResource(userDetails, vehicle)) {
            throw new BadRequestException("User can't add photos to this vehicle");
        }
        if (photos.size() > 5) {
            throw new BadRequestException("Too many photos the maximum is 5");
        }
        AtomicInteger photoNumber = new AtomicInteger(1);
        photos.forEach((photo) -> {
            try {
                byte[] photosBytes = photo.getBytes();
                SerialBlob serialBlob = new SerialBlob(photosBytes);
                VehiclePhoto vehiclePhoto = new VehiclePhoto(vehicle.getName() + "-" + photoNumber, serialBlob, vehicle);
                photoNumber.getAndIncrement();
                photoRepository.save(vehiclePhoto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Transactional
    public byte[] findImageById(Long id) {
        Blob image = findPhotoById(id).getImage();
        try {
            return image.getBytes(1, (int) image.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetails userDetails) {
        VehiclePhoto vehiclePhoto = findPhotoById(id);
        if (userService.isUserOwnerOfResource(userDetails, vehiclePhoto.getVehicle()) || userService.isUserAdmin(userDetails)) {
            photoRepository.delete(vehiclePhoto);
        } else {
            throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
        }
    }

    @Override
    @Transactional
    public void replace(Long photoId, MultipartFile photo, UserDetails userDetails) {
        try {
            VehiclePhoto vehiclePhoto = findPhotoById(photoId);
            if (userService.isUserOwnerOfResource(userDetails, vehiclePhoto.getVehicle()) || userService.isUserAdmin(userDetails)) {
                byte[] photoBytes = photo.getBytes();
                Blob photoBlob = new SerialBlob(photoBytes);
                vehiclePhoto.setImage(photoBlob);
                photoRepository.save(vehiclePhoto);
            } else {
                throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private VehiclePhoto findPhotoById(Long id) {
            return photoRepository.findById(id).orElseThrow(() -> new BadRequestException("Photo not found"));
    }

    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new BadRequestException("Vehicle not found"));
    }
}
