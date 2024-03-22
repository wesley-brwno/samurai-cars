package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import br.com.project.samuraicars.utils.*;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class VehicleServiceTest {

    @Mock
    VehicleRepository vehicleRepositoryMock;
    @Mock
    UserService userServiceMock;
    @InjectMocks
    VehicleServiceImpl vehicleService;

    @BeforeEach
    public void setUp() {
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        List<Vehicle> vehicleList = List.of(vehicle);
        Page<Vehicle> vehiclePage = new PageImpl<>(vehicleList);

        BDDMockito.when(vehicleRepositoryMock.save(ArgumentMatchers.any())).thenReturn(vehicle);
        BDDMockito.when(vehicleRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vehicle));
        BDDMockito.when(vehicleRepositoryMock.findAllByUser(ArgumentMatchers.any())).thenReturn(vehicleList);
        BDDMockito.when(vehicleRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(vehiclePage);
        BDDMockito.doNothing().when(vehicleRepositoryMock).delete(ArgumentMatchers.any(Vehicle.class));
    }

    @Test
    @DisplayName("save should return VehicleResponseBody when successful")
    void save_ShouldReturnVehicleResponseBody_WhenSuccessful() {
        VehicleResponseBody expectedVehicleResponseBody = VehicleResponseBodyCreator.createValidVehicleResponseBody();
        VehiclePostRequestBody validVehiclePostRequestBody = VehiclePostRequestBodyCreator.createValidVehiclePostRequestBody();
        User user = UserCreator.createValidUser();

        VehicleResponseBody vehicleResponseBody = vehicleService.save(validVehiclePostRequestBody, user);

        Assertions.assertThat(vehicleResponseBody)
                .isNotNull()
                .isEqualTo(expectedVehicleResponseBody);
    }

    @Test
    @DisplayName("delete should remove Vehicle when successful")
    void delete_ShouldRemoveVehicle_WhenSuccessful() {
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(ArgumentMatchers.any(UserDetails.class), ArgumentMatchers.any(Vehicle.class))).thenReturn(true);
        BDDMockito.when(userServiceMock.isUserAdmin(ArgumentMatchers.any(UserDetails.class))).thenReturn(true);
        User user = UserCreator.createValidUser();

        Assertions.assertThatCode(() -> vehicleService.delete(1L, user))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user doesn't have permissions")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(ArgumentMatchers.any(UserDetails.class), ArgumentMatchers.any(Vehicle.class))).thenReturn(false);
        BDDMockito.when(userServiceMock.isUserAdmin(ArgumentMatchers.any(UserDetails.class))).thenReturn(false);
        User user = UserCreator.createValidUser();

        Assertions.assertThatCode(() -> vehicleService.delete(1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("replace should update Vehicle when successful")
    void replace_ShouldUpdateVehicle_WhenSuccessful() {
        VehicleResponseBody expectedVehicleResponseBody = VehicleResponseBodyCreator.createUpdatedVehicleResponseBody();
        VehiclePutRequestBody updateVehicleRequest = VehiclePutRequestBodyCreator.createValidVehiclePutRequestBody();
        User user = UserCreator.createValidUser();
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(ArgumentMatchers.any(UserDetails.class), ArgumentMatchers.any(Vehicle.class))).thenReturn(true);
        BDDMockito.when(userServiceMock.isUserAdmin(ArgumentMatchers.any(UserDetails.class))).thenReturn(true);

        VehicleResponseBody vehicleResponseBody = vehicleService.replace(updateVehicleRequest, user);

        Assertions.assertThat(vehicleResponseBody)
                .isNotNull()
                .isEqualTo(expectedVehicleResponseBody);
    }

    @Test
    @DisplayName("replace should throw BadRequestException when user doesn't have permissions")
    void replace_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        VehiclePutRequestBody updateVehicleRequest = VehiclePutRequestBodyCreator.createValidVehiclePutRequestBody();
        User user = UserCreator.createValidUser();
        BDDMockito.when(userServiceMock.isUserOwnerOfResource(ArgumentMatchers.any(UserDetails.class), ArgumentMatchers.any(Vehicle.class))).thenReturn(false);
        BDDMockito.when(userServiceMock.isUserAdmin(ArgumentMatchers.any(UserDetails.class))).thenReturn(false);

        Assertions.assertThatCode(() -> vehicleService.replace(updateVehicleRequest, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("listById should return a VehicleWithPhotosResponseBody when successful")
    void listById_ShouldReturnVehicleWithPhotosResponseBody_WhenSuccessful() {
        VehicleWithPhotosResponseBody expectedVehicleWithPhotosResponseBody = VehicleWithPhotosResponseBodyCreator.createValidVehicleWithPhotosResponseBody();
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);

        VehicleWithPhotosResponseBody vehicleWithPhotosResponseBody = vehicleService.listById(1L, uriComponentsBuilder);

        Assertions.assertThat(vehicleWithPhotosResponseBody)
                .isNotNull()
                .isEqualTo(expectedVehicleWithPhotosResponseBody);
    }

    @Test
    @DisplayName("listAll should return Page of VehicleWithPhotosResponseBody when successful")
    void listAll_ShouldReturnPageOfVehicleWithPhotosResponseBody_WhenSuccessful() {
        VehicleWithPhotosResponseBody responseBody = VehicleWithPhotosResponseBodyCreator.createValidVehicleWithPhotosResponseBody();
        Page<VehicleWithPhotosResponseBody> expectedResponseBodies = new PageImpl<>(List.of(responseBody));
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
        VehicleWithPhotosResponseBody validVehicleWithPhotosResponseBody = VehicleWithPhotosResponseBodyCreator.createValidVehicleWithPhotosResponseBody();
        List<VehicleWithPhotosResponseBody> expectedVehicleWithPhotosResponseBodies = List.of(validVehicleWithPhotosResponseBody);
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);
        User user = UserCreator.createValidUser();

        List<VehicleWithPhotosResponseBody> vehicleWithPhotosResponseBodies = vehicleService.listByUser(user, uriComponentsBuilder);

        Assertions.assertThat(vehicleWithPhotosResponseBodies)
                .isNotEmpty()
                .hasSize(1)
                .isEqualTo(expectedVehicleWithPhotosResponseBodies);
    }
}