package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface VehicleService {
    VehicleResponseBody save(VehiclePostRequestBody vehiclePostRequestBody, UserDetails userDetails);
    void delete(Long vehicleId, UserDetails userDetails);
    VehicleResponseBody replace(VehiclePutRequestBody vehiclePutRequestBody, UserDetails userDetails);
    VehicleWithPhotosResponseBody listById(Long vehicleId, UriComponentsBuilder uriComponentsBuilder);
    Page<VehicleWithPhotosResponseBody> listAll(Pageable pageable, UriComponentsBuilder uriComponentsBuilder);
    List<VehicleWithPhotosResponseBody> listByUser(User user, UriComponentsBuilder uriComponentsBuilder);
    Page<VehicleWithPhotosResponseBody> listByBrand(String brand, Pageable pageable, UriComponentsBuilder uriComponentsBuilder);
    Page<VehicleWithPhotosResponseBody> listByYear(Long year, Pageable pageable, UriComponentsBuilder uriComponentsBuilder);
    Page<VehicleWithPhotosResponseBody> searchByName(String name, Pageable pageable, UriComponentsBuilder uriComponentsBuilder);
    List<String> listBrand();
    List<String> listYears();
    List<String> listSellers();
}
