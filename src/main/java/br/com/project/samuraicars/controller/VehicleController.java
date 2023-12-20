package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.photo.PhotosGetResponseBody;
import br.com.project.samuraicars.DTO.user.VehiclesByUserGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleDetailsGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.repositoy.UserRepository;
import br.com.project.samuraicars.service.UserService;
import br.com.project.samuraicars.service.VehiclePhotoService;
import br.com.project.samuraicars.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final UserRepository userRepository;
    private final VehicleService vehicleService;
    private final VehiclePhotoService photoService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<VehicleGetResponseBody> addVehicle(
            @Valid @RequestBody VehiclePostRequestBody vehicleRequest,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UserDetails user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long vehicleId = vehicleService.save(vehicleRequest, user);
        URI uri = uriComponentsBuilder.path("/vehicle/{vehicle_id}").buildAndExpand(vehicleId).toUri();
        return ResponseEntity.created(uri).body(vehicleService.listById(vehicleId));
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam(name = "vehicle_id") Long vehicleId, Authentication authentication) {
        vehicleService.delete(vehicleId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<VehicleDetailsGetResponseBody>> displayAll(
            @PageableDefault(sort = "createdAt") Pageable pageable, UriComponentsBuilder uriBuilder) {
        Page<VehicleGetResponseBody> vehicles = vehicleService.listAll(pageable);
        Page<VehicleDetailsGetResponseBody> vehicleDetailsPage = vehicles
                .map(vehicle -> new VehicleDetailsGetResponseBody(vehicle,
                        new PhotosGetResponseBody(photoService.getImagesPathByVehicleId(vehicle.id(), uriBuilder)))
                );
        return ResponseEntity.ok().body(vehicleDetailsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDetailsGetResponseBody> displayById(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        VehicleGetResponseBody vehicle = vehicleService.listById(id);
        PhotosGetResponseBody images = new PhotosGetResponseBody(photoService.getImagesPathByVehicleId(vehicle.id(), uriBuilder));
        return ResponseEntity.ok().body(new VehicleDetailsGetResponseBody(vehicle, images));
    }

    @GetMapping()
    public ResponseEntity<VehiclesByUserGetResponseBody> displayByUser(@RequestParam(name = "user_id") Long userId) {
        return ResponseEntity.ok().body(userService.findVehiclesByUser(userId));
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@Valid @RequestBody VehiclePutRequestBody requestBody, Authentication authentication) {
        vehicleService.replace(requestBody, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
