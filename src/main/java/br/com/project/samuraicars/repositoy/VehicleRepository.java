package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT p.id FROM VehiclePhoto p WHERE p.vehicle.id = :vehicleId")
    List<Long> findAllByVehicleId(Long vehicleId);
}
