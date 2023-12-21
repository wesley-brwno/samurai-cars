package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.service.VehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final VehiclePhotoService vehiclePhotoService;

    @PostMapping
    public ResponseEntity<?> save(@RequestParam List<MultipartFile> photos,
                                  @RequestParam Long vehicleId,
                                  UriComponentsBuilder uriBuilder,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.save(photos, vehicleId, userDetails);
        URI uri = uriBuilder.path("/photos").queryParam("vehicle_id", vehicleId).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        vehiclePhotoService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        byte[] image = vehiclePhotoService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping()
    public ResponseEntity<List<String>> getImagesByVehicleId(@RequestParam Long vehicle_id, UriComponentsBuilder uriBuilder) {
        List<String> imagesPathByVehicleId = vehiclePhotoService.getImagesPathByVehicleId(vehicle_id, uriBuilder);
        return ResponseEntity.ok().body(imagesPathByVehicleId);
    }

    @PutMapping("/{photo_id}")
    public ResponseEntity<Void> replace(@PathVariable("photo_id") Long photoId, @RequestParam MultipartFile photo) {
        vehiclePhotoService.replace(photoId, photo);
        return ResponseEntity.noContent().build();
    }
}
