package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUser(User user);
    @Query(value = "SELECT v.brand FROM Vehicle v ORDER BY v.brand")
    List<String> findAllBrand();
}
