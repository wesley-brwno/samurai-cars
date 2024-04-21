package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.user.UserResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    @Override
    @Transactional
    public VehicleResponseBody save(VehiclePostRequestBody vehiclePostRequestBody, UserDetails userDetails) {
        Vehicle vehicle = vehicleRepository.save(mapVehiclePostRequestBodyToEntity(vehiclePostRequestBody, (User) userDetails));
        return mapEntityToVehicleResponseBody(vehicle);
    }

    @Override
    @Transactional
    public void delete(Long vehicleId, UserDetails userDetails) {
        Vehicle vehicle = findById(vehicleId);
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            vehicleRepository.delete(vehicle);
        } else {
            throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
        }
    }

    @Override
    @Transactional
    public VehicleResponseBody replace(VehiclePutRequestBody vehiclePutRequestBody, UserDetails userDetails) {
        Vehicle vehicle = findById(vehiclePutRequestBody.id());
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            Vehicle vehicleUpdated = vehicleRepository.save(mapVehiclePutRequestBodyToEntity(vehiclePutRequestBody, vehicle));
            return mapEntityToVehicleResponseBody(vehicleUpdated);
        }
        throw new BadRequestException("The user is not authorized to perform this operation check their permissions.");
    }

    @Override
    public VehicleWithPhotosResponseBody listById(Long vehicleId, UriComponentsBuilder uriComponentsBuilder) {
        Vehicle vehicle = findById(vehicleId);
        return createVehicleWithPhotosResponseBody(vehicle, uriComponentsBuilder);
    }

    @Override
    public Page<VehicleWithPhotosResponseBody> listAll(Pageable pageable, UriComponentsBuilder uriComponentsBuilder) {
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(pageable);
        return vehiclePage.map(vehicle -> createVehicleWithPhotosResponseBody(vehicle, uriComponentsBuilder));
    }

    @Override
    public List<VehicleWithPhotosResponseBody> listByUser(User user, UriComponentsBuilder uriComponentsBuilder) {
        List<Vehicle> vehicles = vehicleRepository.findAllByUser(user);
        return vehicles.stream().map(vehicle -> createVehicleWithPhotosResponseBody(vehicle, uriComponentsBuilder)).toList();
    }

    @Override
    public List<String> listBrand() {
        return vehicleRepository.findAllBrand().stream().distinct().toList();
    }

    @Override
    public List<String> listYears() {
        return vehicleRepository.findAllYears().stream().distinct().toList();
    }

    @Override
    public Page<VehicleWithPhotosResponseBody> listByBrand(String brand, Pageable pageable, UriComponentsBuilder uriBuilder) {
        Page<Vehicle> vehiclePage = vehicleRepository.findByBrandContaining(brand, pageable);
        return vehiclePage.map(vehicle -> createVehicleWithPhotosResponseBody(vehicle, uriBuilder));
    }

    @Override
    public Page<VehicleWithPhotosResponseBody> listByYear(Long year, Pageable pageable, UriComponentsBuilder uriBuilder) {
        Page<Vehicle> vehiclePage = vehicleRepository.findByYearIs(year, pageable);
        return vehiclePage.map(vehicle -> createVehicleWithPhotosResponseBody(vehicle, uriBuilder));
    }

    private Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new BadRequestException("Vehicle not found!"));
    }

    private VehicleResponseBody mapEntityToVehicleResponseBody(Vehicle vehicle) {
        return VehicleResponseBody.builder()
                .createdAt(vehicle.getCreatedAt())
                .id(vehicle.getId())
                .name(vehicle.getName())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .vehicleType(vehicle.getVehicleType())
                .year(vehicle.getYear())
                .price(vehicle.getPrice())
                .owner(new UserResponseBody(vehicle.getUser().getId(), vehicle.getUser().getName()))
                .build();
    }

    private Vehicle mapVehiclePostRequestBodyToEntity(VehiclePostRequestBody vehicleRequest, User user) {
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

    private Vehicle mapVehiclePutRequestBodyToEntity(VehiclePutRequestBody vehicleRequest, Vehicle vehicle) {
        vehicle.setName(vehicleRequest.name());
        vehicle.setModel(vehicleRequest.model());
        vehicle.setYear(vehicleRequest.year());
        vehicle.setVehicleType(vehicleRequest.vehicleType());
        vehicle.setBrand(vehicleRequest.brand());
        vehicle.setPrice(vehicleRequest.price());
        return vehicle;
    }

    private VehicleWithPhotosResponseBody createVehicleWithPhotosResponseBody(Vehicle vehicle, UriComponentsBuilder uriBuilder) {
        List<String> photoUrls = getPhotoUrls(getPhotoIds(vehicle.getPhotos()), uriBuilder);
        return new VehicleWithPhotosResponseBody(mapEntityToVehicleResponseBody(vehicle), photoUrls);
    }

    private List<Long> getPhotoIds(List<VehiclePhoto> photos) {
        return photos.stream().map(VehiclePhoto::getId).toList();
    }

    private List<String> getPhotoUrls(List<Long> photoIds, UriComponentsBuilder uriComponentsBuilder) {
        String uriString = uriComponentsBuilder.toUriString();
        return photoIds.stream().map(id -> uriString + "/photos/" + id).sorted(String::compareToIgnoreCase).toList();
    }
}
