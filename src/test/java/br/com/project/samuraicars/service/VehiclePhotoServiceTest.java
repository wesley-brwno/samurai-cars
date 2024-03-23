package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehiclePhotoRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class VehiclePhotoServiceTest {
    @Mock
    public VehiclePhotoRepository vehiclePhotoRepository;
    @Mock
    public VehicleRepository vehicleRepository;
    @Mock
    public UserService userService;

    @InjectMocks
    public VehiclePhotoServiceImpl vehiclePhotoService;

    private User validUser;
    private Vehicle validVehicle;
    private VehiclePhoto validVehiclePhoto;

    @BeforeEach
    public void setUp() {
        validUser = createValidUser();
        validVehicle = createValidVehicle();
        validVehiclePhoto = createValidVehiclePhoto();

        BDDMockito.when(vehicleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(validVehicle));
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(validVehiclePhoto));
    }

    @Test
    @DisplayName("save should not throw any exception when successful")
    void save_ShouldNotThrowAnyException_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.save(List.of(photo), 1L, validUser))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("save should throw BadRequestException when user does not have permission")
    void save_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(false);
        User invalidUser = createInvalidUser();

        Assertions.assertThatCode(() -> vehiclePhotoService.save(List.of(photo), 1L, invalidUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User can't add photos to this vehicle");
    }

    @Test
    void save_ShouldThrowBadRequestException_WhenArrayIsTooBig() {
        MultipartFile photo = new MockMultipartFile("image", "someImage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        List<MultipartFile> multipartFiles = List.of(photo, photo, photo, photo, photo, photo);
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.save(multipartFiles, 1L, validUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Too many photos the maximum is 5");
    }

    @Test
    @DisplayName("findImageById should not throw any exception when successful")
    void findImageById_ShouldThrowNoException_WhenSuccessful() {
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(validVehiclePhoto));

        Assertions.assertThatCode(() -> vehiclePhotoService.findImageById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findImageById should throw BadRequestException when image is not found")
    void findImageById_ShouldThrowBadRequestException_WhenImageIsNotFound() {
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> vehiclePhotoService.findImageById(2L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Photo not found");
    }

    @Test
    @DisplayName("delete should not throw any exception when successful")
    void delete_ShouldThrowNoException_WhenSuccessful() {
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.delete(1L, validUser))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user does not have permission")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(false);

        Assertions.assertThatCode(() -> vehiclePhotoService.delete(1L, validUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("replace should not throw any exception when successful")
    void replace_ShouldThrowNoException_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.replace(1L, photo, validUser))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace should throw BadRequestException when user does not have permission")
    void replace_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(false);

        Assertions.assertThatCode(() -> vehiclePhotoService.replace(1L, photo, validUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
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

    private VehiclePhoto createValidVehiclePhoto() {
        Blob mockBlob = Mockito.mock(Blob.class);
        return new VehiclePhoto("photo-1", mockBlob, validVehicle);
    }
}