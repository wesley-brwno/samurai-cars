package br.com.project.samuraicars.service;

import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.model.VehiclePhoto;
import br.com.project.samuraicars.repositoy.VehiclePhotoRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import br.com.project.samuraicars.utils.UserCreator;
import br.com.project.samuraicars.utils.VehicleCreator;
import br.com.project.samuraicars.utils.VehiclePhotoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

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

    @BeforeEach
    public void setUp() {
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        VehiclePhoto vehiclePhoto = VehiclePhotoCreator.createValidVehiclePhoto();

        BDDMockito.when(vehicleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(vehicle));
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vehiclePhoto));
    }

    @Test
    @DisplayName("save should not throw any exception when successful")
    void save_ShouldNotThrowAnyException_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        User user = UserCreator.createValidUser();
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.save(List.of(photo), 1L, user))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("save should throw BadRequestException when user does not have permission")
    void save_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        User user = UserCreator.createValidUser();
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(false);

        Assertions.assertThatCode(() -> vehiclePhotoService.save(List.of(photo), 1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User can't add photos to this vehicle");
    }

    @Test
    void save_ShouldThrowBadRequestException_WhenArrayIsTooBig() {
        MultipartFile photo = new MockMultipartFile("image", "someImage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        List<MultipartFile> multipartFiles = List.of(photo, photo, photo, photo, photo, photo);
        User user = UserCreator.createValidUser();
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.save(multipartFiles, 1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Too many photos the maximum is 5");
    }

    @Test
    @DisplayName("findImageById should not throw any exception when successful")
    void findImageById_ShouldThrowNoException_WhenSuccessful() {
        VehiclePhoto vehiclePhoto = VehiclePhotoCreator.createValidVehiclePhoto();
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vehiclePhoto));

        Assertions.assertThatCode(() -> vehiclePhotoService.findImageById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findImageById should throw BadRequestException when image is not found")
    void findImageById_ShouldThrowBadRequestException_WhenImageIsNotFound() {
        BDDMockito.when(vehiclePhotoRepository.findById(ArgumentMatchers.anyLong())).thenThrow(BadRequestException.class);

        Assertions.assertThatCode(() -> vehiclePhotoService.findImageById(2L))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("delete should not throw any exception when successful")
    void delete_ShouldThrowNoException_WhenSuccessful() {
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(true);
        User user = UserCreator.createValidUser();

        Assertions.assertThatCode(() -> vehiclePhotoService.delete(1L, user))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user does not have permission")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
        BDDMockito.when(userService.isUserAdmin(ArgumentMatchers.any())).thenReturn(false);
        User user = UserCreator.createValidUser();

        Assertions.assertThatCode(() -> vehiclePhotoService.delete(1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }

    @Test
    @DisplayName("replace should not throw any exception when successful")
    void replace_ShouldThrowNoException_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        User user = UserCreator.createValidUser();
        BDDMockito.doNothing().when(vehiclePhotoRepository).delete(ArgumentMatchers.any(VehiclePhoto.class));
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        Assertions.assertThatCode(() -> vehiclePhotoService.replace(1L, photo, user))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace should throw BadRequestException when user does not have permission")
    void replace_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        MultipartFile photo = new MockMultipartFile("image", "myimage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        User user = UserCreator.createValidUser();
        BDDMockito.when(userService.isUserOwnerOfResource(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);

        Assertions.assertThatCode(() -> vehiclePhotoService.replace(1L, photo, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user is not authorized to perform this operation check their permissions.");
    }
}