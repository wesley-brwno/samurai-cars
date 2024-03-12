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
        return mapVehicleToVehicleDetailsGetResponseBody(vehicle, uriBuilder);
    }

    public Page<VehicleDetailsGetResponseBody> listAll(Pageable pageable, UriComponentsBuilder uriBuilder) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);
        return vehiclesPage.map(vehicle -> mapVehicleToVehicleDetailsGetResponseBody(vehicle, uriBuilder));
    }

    public List<VehicleDetailsGetResponseBody> listAllByUser(User user, UriComponentsBuilder uriBuilder) {
        List<Vehicle> vehicles = vehicleRepository.findAllByUser(user);
        return vehicles.stream().map(vehicle -> mapVehicleToVehicleDetailsGetResponseBody(vehicle, uriBuilder)).toList();
    }

    @Transactional
    public Vehicle save(VehiclePostRequestBody vehicleRequest, UserDetails user) {
        return vehicleRepository.save(mapVehiclePostRequestBodyToVehicle(vehicleRequest, (User) user));
    }

    @Transactional
    public void delete(Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = findById(vehicleId);
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
        }
    }

    @Transactional
    public Vehicle replace(VehiclePutRequestBody requestBody, UserDetails userDetails) {
        Vehicle vehicle = findById(requestBody.id());
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            return vehicleRepository.save(mapVehiclePutRequestBodyToVehicle(requestBody, vehicle));
        }
        throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
    }

    public Vehicle findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new BadRequestException("Vehicle not found!"));
    }

    private List<Long> getVehiclePhotosId(List<VehiclePhoto> photosList) {
        return photosList.stream().map(VehiclePhoto::getId).toList();
    }

    private VehicleDetailsGetResponseBody mapVehicleToVehicleDetailsGetResponseBody(Vehicle vehicle, UriComponentsBuilder uriBuilder) {
        List<String> photoUrls = vehiclePhotoService.getPhotosUrlByVehicleId(getVehiclePhotosId(vehicle.getPhotos()), uriBuilder);
        return new VehicleDetailsGetResponseBody(new VehicleGetResponseBody(vehicle), photoUrls);
    }

    private Vehicle mapVehiclePostRequestBodyToVehicle(VehiclePostRequestBody vehicleRequest, User user) {
        return Vehicle.builder()
                .name(vehicleRequest.name())
                .model(vehicleRequest.model())
                .year(vehicleRequest.year())
                .vehicleType(vehicleRequest.vehicleType())
                .brand(vehicleRequest.brand())
                .price(vehicleRequest.price())
                .user(user)
                .build();
    }

    private Vehicle mapVehiclePutRequestBodyToVehicle(VehiclePutRequestBody vehicleRequest, Vehicle vehicle) {
        vehicle.setName(vehicleRequest.name());
        vehicle.setModel(vehicleRequest.model());
        vehicle.setYear(vehicleRequest.year());
        vehicle.setVehicleType(vehicleRequest.vehicleType());
        vehicle.setBrand(vehicleRequest.brand());
        vehicle.setPrice(vehicleRequest.price());
        return vehicle;
    }
}
