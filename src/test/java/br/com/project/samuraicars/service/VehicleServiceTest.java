package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class VehicleServiceTest {

    @Mock
    public VehicleRepository vehicleRepositoryMock;
    @Mock
    public UserService userServiceMock;
    @InjectMocks
    public VehicleServiceImpl vehicleService;

    private User validUser;
    private Vehicle validVehicle;
    private VehiclePostRequestBody validVehiclePostRequestBody;
    private VehicleResponseBody validVehicleResponseBody;
    private VehicleWithPhotosResponseBody validVehicleWithPhotosResponseBody;

    @BeforeEach
    public void setUp() {
        validUser = createValidUser();
        validVehicle = createValidVehicle();
        validVehicleResponseBody = createValidVehicleResponseBody();
        validVehiclePostRequestBody = createValidVehiclePostRequestBody();
        validVehicleWithPhotosResponseBody = createValidVehicleWithPhotosResponseBody();

        List<Vehicle> vehicleList = List.of(validVehicle);
        Page<Vehicle> vehiclePage = new PageImpl<>(vehicleList);

        BDDMockito.when(vehicleRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(validVehicle));
        BDDMockito.when(vehicleRepositoryMock.findAllByUser(ArgumentMatchers.any())).thenReturn(vehicleList);
        BDDMockito.when(vehicleRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(vehiclePage);
        BDDMockito.doNothing().when(vehicleRepositoryMock).delete(ArgumentMatchers.any(Vehicle.class));
    }

    @Test
    @DisplayName("save should return VehicleResponseBody when successful")
    void save_ShouldReturnVehicleResponseBody_WhenSuccessful() {
        BDDMockito.when(vehicleRepositoryMock.save(ArgumentMatchers.any())).thenReturn(validVehicle);

        VehicleResponseBody vehicleResponseBody = vehicleService.save(validVehiclePostRequestBody, validUser);

        Assertions.assertThat(vehicleResponseBody)
                .isNotNull()
                .isEqualTo(validVehicleResponseBody);
    }

    @Test
    @DisplayName("delete should remove Vehicle when successful")
    void delete_ShouldRemoveVehicle_WhenSuccessful() {
        BDDMockito.doNothing().when(vehicleRepositoryMock).delete(ArgumentMatchers.any(Vehicle.class));
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);

        Assertions.assertThatCode(() -> vehicleService.delete(1L, validUser))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user doesn't have permissions")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(ArgumentMatchers.any(UserDetails.class), ArgumentMatchers.any(Vehicle.class))).thenReturn(false);

        Assertions.assertThatCode(() -> vehicleService.delete(1L, validUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("replace should update Vehicle when successful")
    void replace_ShouldUpdateVehicle_WhenSuccessful() {
        VehiclePutRequestBody validRequestBody = createValidVehiclePutRequestBody();
        VehicleResponseBody expectedResponseBody = createUpdatedVehicleResponseBody();
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);
        BDDMockito.when(vehicleRepositoryMock.save(ArgumentMatchers.any(Vehicle.class))).thenReturn(validVehicle);

        VehicleResponseBody responseBody = vehicleService.replace(validRequestBody, validUser);

        Assertions.assertThat(responseBody)
                .isNotNull()
                .isEqualTo(expectedResponseBody);
    }

    @Test
    @DisplayName("replace should throw BadRequestException when user doesn't have permissions")
    void replace_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        VehiclePutRequestBody validRequestBody = createValidVehiclePutRequestBody();
        User invalidUser = createInvalidUser();
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(invalidUser, validVehicle)).thenReturn(false);
        BDDMockito.when(vehicleRepositoryMock.save(ArgumentMatchers.any(Vehicle.class))).thenReturn(validVehicle);

        Assertions.assertThatCode(() -> vehicleService.replace(validRequestBody, invalidUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("listById should return a VehicleWithPhotosResponseBody when successful")
    void listById_ShouldReturnVehicleWithPhotosResponseBody_WhenSuccessful() {
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);

        VehicleWithPhotosResponseBody vehicleWithPhotosResponseBody = vehicleService.listById(1L, uriComponentsBuilder);

        Assertions.assertThat(vehicleWithPhotosResponseBody)
                .isNotNull()
                .isEqualTo(validVehicleWithPhotosResponseBody);
    }

    @Test
    @DisplayName("listById should throw BadRequestException when Vehicle is not found")
    void listById_ShouldThrowBadRequestException_WhenVehicleIsNotFound() {
        BDDMockito.when(vehicleRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);

        Assertions.assertThatCode(() -> vehicleService.listById(1L, uriComponentsBuilder))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Vehicle not found!");
    }

    @Test
    @DisplayName("listAll should return Page of VehicleWithPhotosResponseBody when successful")
    void listAll_ShouldReturnPageOfVehicleWithPhotosResponseBody_WhenSuccessful() {
        Page<VehicleWithPhotosResponseBody> expectedResponseBodies = new PageImpl<>(List.of(validVehicleWithPhotosResponseBody));
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);
        PageRequest pageable = PageRequest.of(0, 10);

        Page<VehicleWithPhotosResponseBody> responseBodies = vehicleService.listAll(pageable, uriComponentsBuilder);

        Assertions.assertThat(responseBodies)
                .isNotEmpty()
                .hasSize(1)
                .isEqualTo(expectedResponseBodies);
    }

    @Test
    @DisplayName("listByUser should return a List of VehicleWithPhotosResponseBody when successful")
    void listByUser_ShouldReturnListOfVehicleWithPhotosResponseBody_WhenSuccessful() {
        List<VehicleWithPhotosResponseBody> expectedVehicleWithPhotosResponseBodies = List.of(validVehicleWithPhotosResponseBody);
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);

        List<VehicleWithPhotosResponseBody> vehicleWithPhotosResponseBodies = vehicleService.listByUser(validUser, uriComponentsBuilder);

        Assertions.assertThat(vehicleWithPhotosResponseBodies)
                .isNotEmpty()
                .hasSize(1)
                .isEqualTo(expectedVehicleWithPhotosResponseBodies);
    }

    private User createValidUser() {
        return User.builder()
                .id(1L)
                .name("Tom")
                .email("tom@email.com")
                .authorities("USER")
                .build();
    }

    private User createInvalidUser() {
        return User.builder()
                .id(2L)
                .name("Zoro")
                .email("zoro@email.com")
                .authorities("USER")
                .build();
    }

    private Vehicle createValidVehicle() {
        VehiclePhoto vehiclePhoto = new VehiclePhoto();
        vehiclePhoto.setId(1L);
        vehiclePhoto.setName("vehicle_photo");
        Blob mockBlob = Mockito.mock(Blob.class);
        vehiclePhoto.setImage(mockBlob);

        return Vehicle.builder()
                .id(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("2 doors Fastback")
                .brand("Volkswagen")
                .price(5000.00)
                .user(validUser)
                .photos(List.of(vehiclePhoto))
                .build();
    }

    private VehicleResponseBody createValidVehicleResponseBody() {
        return VehicleResponseBody.builder()
                .id(1L)
                .userId(1L)
                .name("Fusca")
                .model("1300 GL")
                .year(1991L)
                .vehicleType("2 doors Fastback")
                .brand("Volkswagen")
                .price(5000.00)
                .build();
    }

    private VehiclePostRequestBody createValidVehiclePostRequestBody() {
        return new VehiclePostRequestBody(
                "Fusca",
                "1300 GL",
                1991L,
                "2 doors Fastback",
                "Volkswagen",
                5000.00
        );
    }

    private VehiclePutRequestBody createValidVehiclePutRequestBody() {
        return new VehiclePutRequestBody(
                1L,
                "Civic",
                "EX",
                2022L,
                "sedan",
                "Honda",
                25000.00
        );
    }

    private VehicleResponseBody createUpdatedVehicleResponseBody() {
        return VehicleResponseBody.builder()
                .id(1L)
                .userId(1L)
                .name("Civic")
                .model("EX")
                .year(2022L)
                .vehicleType("sedan")
                .brand("Honda")
                .price(25000.00)
                .build();
    }

    private VehicleWithPhotosResponseBody createValidVehicleWithPhotosResponseBody() {
        List<String> photos = List.of("null/photos/1");
        return new VehicleWithPhotosResponseBody(validVehicleResponseBody, photos);
    }
}