package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUser(User user);
    @Query(value = "SELECT v.brand FROM Vehicle v ORDER BY v.brand")
    List<String> findAllBrand();
    @Query(value = "SELECT v.year FROM Vehicle v ORDER BY v.year")
    List<String> findAllYears();
    @Query("SELECT u.name FROM User u ORDER BY u.name")
    List<String> findAllUserName();
    Page<Vehicle> findByBrandContaining(String brand, Pageable pageable);
    Page<Vehicle> findByYearIs(Long year, Pageable pageable);
    Page<Vehicle> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
