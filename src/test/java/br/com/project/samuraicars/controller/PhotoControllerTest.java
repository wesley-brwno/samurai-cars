package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.VehiclePhotoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class PhotoControllerTest {

    @Mock
    private VehiclePhotoServiceImpl vehiclePhotoService;
    @InjectMocks
    private PhotoController photoController;

    private UserDetails userDetails;
    @BeforeEach
    void setUp() {
        userDetails = createValidUser();
    }

    @Test
    @DisplayName("save should return status code Created when successful")
    void save_ShouldReturnStatusCodeCreated_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "someImage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.doNothing().when(vehiclePhotoService).save(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        ResponseEntity<?> responseEntity = photoController.save(List.of(photo), 1L, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("delete should return status code Gone when successful")
    void delete_ShouldReturnStatusCodeGone_WhenSuccessful() {
        BDDMockito.doNothing().when(vehiclePhotoService).delete(ArgumentMatchers.anyLong(), ArgumentMatchers.any(UserDetails.class));

        ResponseEntity<?> responseEntity = photoController.delete(1L, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
    }

    @Test
    @DisplayName("findById should return status code OK when successful")
    void findById() throws IOException {
        MultipartFile photo = new MockMultipartFile("image", "someImage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.when(vehiclePhotoService.findImageById(ArgumentMatchers.anyLong())).thenReturn(photo.getBytes());

        ResponseEntity<?> responseEntity = photoController.findById(1L);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("replace should return status code No Content when successful")
    void replace_ShouldReturnStatusCodeNoContent_WhenSuccessful() {
        MultipartFile photo = new MockMultipartFile("image", "someImage.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
        BDDMockito.doNothing().when(vehiclePhotoService).replace(ArgumentMatchers.anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any());

        ResponseEntity<Void> responseEntity = photoController.replace(1L, photo, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
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