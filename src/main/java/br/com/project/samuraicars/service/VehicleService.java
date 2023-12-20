package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.vehicle.VehicleGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserService userService;
    public List<VehicleGetResponseBody> listAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(vehicle -> new VehicleGetResponseBody(
                        vehicle.getCreatedAt(),
                        vehicle.getId(),
                        vehicle.getName(),
                        vehicle.getModelo(),
                        vehicle.getYear(),
                        vehicle.getUser().getId()))
                .toList();
    }

    public Page<VehicleGetResponseBody> listAll(Pageable pageable) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);
        return vehiclesPage.map(vehicle -> new VehicleGetResponseBody(
                vehicle.getCreatedAt(),
                vehicle.getId(),
                vehicle.getName(),
                vehicle.getModelo(),
                vehicle.getYear(),
                vehicle.getUser().getId())
        );
    }

    public VehicleGetResponseBody listById(Long vehicleId) {
        Vehicle vehicle = findById(vehicleId);
        return new VehicleGetResponseBody(
                vehicle.getCreatedAt(),
                vehicle.getId(),
                vehicle.getName(),
                vehicle.getModelo(),
                vehicle.getYear(),
                vehicle.getUser().getId()
        );
    }

    @Transactional
    public Long save(VehiclePostRequestBody vehicleRequest, UserDetails user) {
        Vehicle vehicle = new Vehicle(vehicleRequest, user);
        return vehicleRepository.save(vehicle).getId();
    }

    public void delete(Long vehicleId, String userEmail) {
        UserDetails userDetails = userService.findByEmail(userEmail);
        Vehicle vehicle = findById(vehicleId);
        if (userService.isUserOwnerOfResource(userDetails, vehicle) || userService.isUserAdmin(userDetails)) {
            vehicleRepository.delete(vehicle);
        }
    }

    public Vehicle replace(VehiclePutRequestBody requestBody, String email) {
        UserDetails userDetails = userService.findByEmail(email);
        Vehicle vehicle = findById(requestBody.id());
        if (userService.isUserOwnerOfResource(userDetails, vehicle)) {
            vehicle.setName(requestBody.name());
            vehicle.setModelo(requestBody.model());
            vehicle.setYear(requestBody.year());
            vehicleRepository.save(vehicle);
        }
        return vehicle;
    }

    private Vehicle findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new BadRequestException("Vehicle not found!"));
    }
}
