package br.com.project.samuraicars.repositoy;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;

    private Vehicle validVehicle;
    private User validUser;

    @BeforeEach
    void setUp() {
        validVehicle = createValidVehicle();
        validUser = createValidUser();
    }

    @Test
    @DisplayName("save should persist Vehicle when successful")
    void save_ShouldPersistVehicle_WhenSuccessful() {
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setId(null);
        userRepository.save(validUser);


        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);

        Assertions.assertThat(vehicleSaved).isNotNull();
        Assertions.assertThat(vehicleSaved.getId()).isNotNull();
        Assertions.assertThat(vehicleSaved.getName()).isEqualTo(validVehicle.getName());
    }

    @Test
    @DisplayName("save should update Vehicle when successful")
    void save_ShouldUpdateVehicle_WhenSuccessful() {
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setId(null);
        userRepository.save(validUser);
        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);
        vehicleSaved.setName("Civic");

        Vehicle vehicleUpdated = vehicleRepository.save(vehicleSaved);

        Assertions.assertThat(vehicleUpdated).isNotNull();
        Assertions.assertThat(vehicleUpdated.getId()).isEqualTo(vehicleSaved.getId());
        Assertions.assertThat(vehicleUpdated.getName()).isNotEqualTo(validVehicle.getName());
    }

    @Test
    @DisplayName("findAllByUser should return a List of Vehicle when successful")
    void findAllByUser_ShouldReturnAListOfVehicle_WhenSuccessful() {
        User savedUser = userRepository.save(validUser);
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setUser(savedUser);
        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);

        List<Vehicle> vehicles = vehicleRepository.findAllByUser(savedUser);

        Assertions.assertThat(vehicles)
                .isNotEmpty()
                .hasSize(1)
                .contains(vehicleSaved);
    }

    @Test
    @DisplayName("findById should return Vehicle when successful")
    void findById_ShouldReturnVehicle_WhenSuccessful() {
        userRepository.save(validUser);
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setId(null);
        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleSaved.getId());

        Assertions.assertThat(vehicleOptional.isPresent()).isTrue();
        Assertions.assertThat(vehicleOptional.get()).isEqualTo(vehicleSaved);
    }

    @Test
    @DisplayName("findAll should return Page of Vehicle when successful")
    void findAll_ShouldReturnPageOfVehicle_WhenSuccessful() {
        User savedUser = userRepository.save(validUser);
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setUser(savedUser);
        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);

        Page<Vehicle> vehiclePage = vehicleRepository.findAll(PageRequest.of(0, 10));

        Assertions.assertThat(vehiclePage.isEmpty()).isFalse();
        Assertions.assertThat(vehiclePage.getContent().get(0)).isEqualTo(vehicleSaved);
    }

    @Test
    @DisplayName("delete should remove Vehicle when successful")
    void delete_ShouldRemoveVehicle_WhenSuccessful() {
        userRepository.save(validUser);
        Vehicle vehicleToBeSaved = createValidVehicle();
        vehicleToBeSaved.setId(null);
        Vehicle vehicleSaved = vehicleRepository.save(vehicleToBeSaved);

        Assertions.assertThatCode(() -> vehicleRepository.delete(vehicleSaved))
                .doesNotThrowAnyException();

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(1L);

        Assertions.assertThat(vehicleOptional.isEmpty()).isTrue();
    }

    private Vehicle createValidVehicle() {

        User validUser = createValidUser();

        return Vehicle.builder()
                .id(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("2 doors Fastback")
                .brand("Volkswagen")
                .price(5000.00)
                .user(validUser)
                .build();
    }

    private User createValidUser() {
        return User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("USER")
                .build();
    }
}