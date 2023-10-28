package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.VehichlePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclePhotoRepository extends JpaRepository<VehichlePhoto, Long> {
}
