package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.VehiclePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclePhotoRepository extends JpaRepository<VehiclePhoto, Long> {
}
