package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUser(User user);
}
