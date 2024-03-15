package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.service.UserService;
import br.com.project.samuraicars.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping()
    public ResponseEntity<VehicleResponseBody> save(@Valid @RequestBody VehiclePostRequestBody vehicleRequest,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(vehicleService.save(vehicleRequest, userDetails), HttpStatus.CREATED);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam(name = "vehicle_id") Long vehicleId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        vehicleService.delete(vehicleId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<VehicleWithPhotosResponseBody>> displayAll(
            @PageableDefault(sort = "createdAt") Pageable pageable, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listAll(pageable, uriBuilder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleWithPhotosResponseBody> displayById(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listById(id, uriBuilder));
    }

    @GetMapping(params = {"user_id"})
    public ResponseEntity<List<VehicleWithPhotosResponseBody>> displayByUser(
            @RequestParam(name = "user_id") Long userId, UriComponentsBuilder uriBuilder) {
        User user = userService.findById(userId);
        return ResponseEntity.ok().body(vehicleService.listByUser(user, uriBuilder));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PutMapping()
    public ResponseEntity<VehicleResponseBody> replace(
            @Valid @RequestBody VehiclePutRequestBody requestBody, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(vehicleService.replace(requestBody, userDetails));
    }
}
