package br.com.project.samuraicars.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehiclePhotoService {
    void save(List<MultipartFile> photos, Long vehicleId, UserDetails userDetails);
    byte[] findImageById(Long id);
    void delete(Long id, UserDetails userDetails);
    void replace(Long photoId, MultipartFile photo, UserDetails userDetails);
}
