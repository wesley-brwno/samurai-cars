package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRespository extends JpaRepository<Vehicle, Long> {
}
