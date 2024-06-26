package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.service.VehiclePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
@Tag(name = "Photo")
public class PhotoController {
    private final VehiclePhotoService vehiclePhotoService;

    @PostMapping
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Save an array of multipart (images)")
    public ResponseEntity<?> save(@RequestPart("photos") @Valid @NotNull @Size(min = 1, max = 5) List<MultipartFile> photos,
                                  @RequestParam Long vehicleId, @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.save(photos, vehicleId, userDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Remove an image by its id")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieve an image by its id")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        byte[] image = vehiclePhotoService.findImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @PutMapping("/{photo_id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Update an image")
    public ResponseEntity<Void> replace(@PathVariable("photo_id") Long photoId, @RequestParam MultipartFile photo,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.replace(photoId, photo, userDetails);
        return ResponseEntity.noContent().build();
    }
}
