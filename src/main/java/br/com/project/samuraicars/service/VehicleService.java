package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.photo.PhotosGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleDetailsGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehiclePhotoService vehiclePhotoService;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    public VehicleDetailsGetResponseBody listById(Long vehicleId, UriComponentsBuilder uriBuilder) {
        Vehicle vehicle = findById(vehicleId);
        return new VehicleDetailsGetResponseBody(new VehicleGetResponseBody(vehicle),
                new PhotosGetResponseBody(vehiclePhotoService.getPhotosUrlByVehicleId(
                        getVehiclePhotosId(vehicle.getPhotos()), uriBuilder)
                )
        );
    }

    public Page<VehicleDetailsGetResponseBody> listAll(Pageable pageable, UriComponentsBuilder uriBuilder) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);
        return vehiclesPage.map(vehicle -> new VehicleDetailsGetResponseBody(new VehicleGetResponseBody(vehicle),
                new PhotosGetResponseBody(vehiclePhotoService.getPhotosUrlByVehicleId(getVehiclePhotosId(vehicle.getPhotos()), uriBuilder)))
        );
    }

    public List<VehicleDetailsGetResponseBody> listAllByUser(User user, UriComponentsBuilder uriBuilder) {
        List<Vehicle> vehicles = vehicleRepository.findAllByUser(user);
        List<VehicleDetailsGetResponseBody> detailedVehicleBody = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            VehicleGetResponseBody vehicleGetResponseBody = new VehicleGetResponseBody(vehicle);
            detailedVehicleBody.add(new VehicleDetailsGetResponseBody(vehicleGetResponseBody,
                            new PhotosGetResponseBody(vehiclePhotoService.getPhotosUrlByVehicleId(
                                    getVehiclePhotosId(vehicle.getPhotos()), uriBuilder))
                    )
            );
        }
        return detailedVehicleBody;
    }

    @Transactional
    public Vehicle save(VehiclePostRequestBody vehicleRequest, UserDetails user) {
        Vehicle vehicle = new Vehicle(vehicleRequest, user);
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = findById(vehicleId);
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
        }
    }

    public Vehicle replace(VehiclePutRequestBody requestBody, UserDetails userDetails) {
        Vehicle vehicle = findById(requestBody.id());
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            vehicle.setName(requestBody.name());
            vehicle.setModel(requestBody.model());
            vehicle.setYear(requestBody.year());
            vehicleRepository.save(vehicle);
        } else {
            throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
        }
        return vehicle;
    }

    public Vehicle findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new BadRequestException("Vehicle not found!"));
    }

    private List<Long> getVehiclePhotosId(List<VehiclePhoto> photosList) {
        return photosList.stream().map(VehiclePhoto::getId).toList();
    }
}
