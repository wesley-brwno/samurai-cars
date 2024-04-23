package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.UserService;
import br.com.project.samuraicars.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;


@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle")
public class VehicleController {
    private final VehicleService vehicleService;
    private final UserService userService;

    @PostMapping()
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Save vehicle data")
    public ResponseEntity<VehicleResponseBody> save(@Valid @RequestBody VehiclePostRequestBody vehicleRequest,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(vehicleService.save(vehicleRequest, userDetails), HttpStatus.CREATED);
    }

    @DeleteMapping()
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Remove a vehicle by its id")
    public ResponseEntity<Void> delete(@RequestParam(name = "vehicle_id") Long vehicleId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        vehicleService.delete(vehicleId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @Operation(description = "Retrieve a pageable of vehicles")
    public ResponseEntity<Page<VehicleWithPhotosResponseBody>> displayAll(
            @PageableDefault(sort = "createdAt") Pageable pageable, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listAll(pageable, uriBuilder));
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieve a vehicle by its id")
    public ResponseEntity<VehicleWithPhotosResponseBody> displayById(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok().body(vehicleService.listById(id, uriBuilder));
    }

    @GetMapping(params = {"user_id"})
    @Operation(description = "Retrieve a pageable of vehicle by its owner")
    public ResponseEntity<List<VehicleWithPhotosResponseBody>> displayByUser(
            @RequestParam(name = "user_id") Long userId, UriComponentsBuilder uriBuilder) {
        User user = userService.findById(userId);
        return ResponseEntity.ok().body(vehicleService.listByUser(user, uriBuilder));
    }

    @PutMapping()
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Update a vehicle")
    public ResponseEntity<VehicleResponseBody> replace(
            @Valid @RequestBody VehiclePutRequestBody requestBody, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(vehicleService.replace(requestBody, userDetails));
    }

    @GetMapping("/brand")
    public ResponseEntity<List<String>> listBrand() {
        return ResponseEntity.ok(vehicleService.listBrand());
    }

    @GetMapping("/year")
    public ResponseEntity<List<String>> listYear() {
        return ResponseEntity.ok(vehicleService.listYears());
    }

    @GetMapping("/seller")
    public ResponseEntity<List<String>> listSeller() {
        return ResponseEntity.ok(vehicleService.listSellers());
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<VehicleWithPhotosResponseBody>> displayByBrand(
            @PathVariable String brand, Pageable pageable,
            UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.ok(vehicleService.listByBrand(brand,pageable, uriComponentsBuilder));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Page<VehicleWithPhotosResponseBody>> displayByYear(
            @PathVariable Long year, Pageable pageable,
            UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.ok(vehicleService.listByYear(year, pageable, uriComponentsBuilder));
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<Page<VehicleWithPhotosResponseBody>> searchByName(
            @RequestParam String name, @PageableDefault(sort = "name") Pageable pageable,
            UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.ok(vehicleService.searchByName(name, pageable, uriComponentsBuilder));
    }
}
