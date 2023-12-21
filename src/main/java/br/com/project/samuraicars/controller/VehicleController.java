package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.vehicle.VehicleDetailsGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleGetResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.service.UserService;
import br.com.project.samuraicars.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<VehicleGetResponseBody> addVehicle(
            @Valid @RequestBody VehiclePostRequestBody vehicleRequest, @AuthenticationPrincipal UserDetails user,
            UriComponentsBuilder uriComponentsBuilder) {
        Vehicle vehicle = vehicleService.save(vehicleRequest, user);
        URI uri = uriComponentsBuilder.path("/vehicle/{vehicle_id}").buildAndExpand(vehicle.getId()).toUri();
        return ResponseEntity.created(uri).body(new VehicleGetResponseBody(vehicle));
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam(name = "vehicle_id") Long vehicleId, Authentication authentication) {
        vehicleService.delete(vehicleId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<VehicleDetailsGetResponseBody>> displayAll(
            @PageableDefault(sort = "createdAt") Pageable pageable, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listAll(pageable, uriBuilder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDetailsGetResponseBody> displayById(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listById(id, uriBuilder));
    }

    @GetMapping()
    public ResponseEntity<List<VehicleDetailsGetResponseBody>> displayByUser(
            @RequestParam(name = "user_id") Long userId, UriComponentsBuilder uriBuilder) {
        User user = userService.findById(userId);
        return ResponseEntity.ok().body(vehicleService.listAllByUser(user, uriBuilder));
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@Valid @RequestBody VehiclePutRequestBody requestBody, Authentication authentication) {
        vehicleService.replace(requestBody, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
