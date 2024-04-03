package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.vehicle.VehiclePostRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehiclePutRequestBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleResponseBody;
import br.com.project.samuraicars.DTO.vehicle.VehicleWithPhotosResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.UserService;
import br.com.project.samuraicars.service.VehicleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private VehicleController vehicleController;
    @Mock
    private VehicleServiceImpl vehicleService;
    @Mock
    private UserService userService;

    private VehicleResponseBody vehicleResponseBody;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = createValidUser();
        vehicleResponseBody = createValidVehicleResponseBody();
    }

    @Test
    @DisplayName("save should return status code Created when successful")
    void save_ShouldReturnStatusCodeCreated_WhenSuccessful() {
        VehiclePostRequestBody postRequestBody = createValidVehiclePostRequestBody();
        BDDMockito.when(vehicleService.save(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(vehicleResponseBody);

        ResponseEntity<VehicleResponseBody> responseEntity = vehicleController.save(postRequestBody, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(vehicleResponseBody);

        BDDMockito.verify(vehicleService).save(eq(postRequestBody), eq(userDetails));
    }

    @Test
    @WithMockUser()
    @DisplayName("replace should return Bad Request when request body is invalid")
    void save_ShouldThrowBadRequest_WhenDataIsInvalid() throws Exception {
        VehiclePostRequestBody invalidRequestBody = createInvalidVehiclePostRequestBody();

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("delete should return status code No Content when successful")
    void delete_ShouldReturnNoContent_WhenSuccessful() {
        BDDMockito.doNothing().when(vehicleService).delete(ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        ResponseEntity<Void> response = vehicleController.delete(1L, userDetails);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("displayAll should return status code OK when successful")
    void displayAll_ShouldReturnStatusCodeOK_WhenSuccessful() {
        Page<VehicleWithPhotosResponseBody> vehicleResponseBodies = new PageImpl<>(List.of(new VehicleWithPhotosResponseBody(vehicleResponseBody, List.of("SomeImage/1"))));
        BDDMockito.when(vehicleService.listAll(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(vehicleResponseBodies);
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);

        ResponseEntity<Page<VehicleWithPhotosResponseBody>> responseEntity = vehicleController.displayAll(PageRequest.of(0, 10), uriComponentsBuilder);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("displayById should return status code OK when successful")
    void displayById_ShouldReturnStatusCodeOK_WhenSuccessful() {
        VehicleWithPhotosResponseBody vehicleWithPhotosResponseBody = new VehicleWithPhotosResponseBody(vehicleResponseBody, List.of("SomeImage/1"));
        UriComponentsBuilder componentsBuilder = Mockito.mock(UriComponentsBuilder.class);
        BDDMockito.when(vehicleService.listById(1L, componentsBuilder)).thenReturn(vehicleWithPhotosResponseBody);

        ResponseEntity<VehicleWithPhotosResponseBody> responseEntity = vehicleController.displayById(1L, componentsBuilder);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(vehicleWithPhotosResponseBody);
    }

    @Test
    @DisplayName("displayByUser should return status code OK when successful")
    void displayByUser_ShouldReturnStatusCodeOK_WhenSuccessful() {
        VehicleWithPhotosResponseBody vehicleWithPhotosResponseBody = new VehicleWithPhotosResponseBody(vehicleResponseBody, List.of("SomeImage/1"));
        UriComponentsBuilder uriComponentsBuilder = Mockito.mock(UriComponentsBuilder.class);
        BDDMockito.when(userService.findById(ArgumentMatchers.anyLong())).thenReturn((User) userDetails);
        BDDMockito.when(vehicleService.listByUser((User) userDetails, uriComponentsBuilder)).thenReturn(List.of(vehicleWithPhotosResponseBody));

        ResponseEntity<List<VehicleWithPhotosResponseBody>> responseEntity = vehicleController.displayByUser(1L, uriComponentsBuilder);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody())
                .isNotEmpty()
                .hasSize(1)
                .contains(vehicleWithPhotosResponseBody);
    }

    @Test
    @DisplayName("replace should return status code OK when successful")
    void replace_ShouldReturnStatusCodeOK_WhenSuccessful() {
        VehiclePutRequestBody putRequestBody = createValidVehiclePutRequestBody();
        BDDMockito.when(vehicleService.replace(putRequestBody, userDetails)).thenReturn(vehicleResponseBody);

        ResponseEntity<VehicleResponseBody> responseEntity = vehicleController.replace(putRequestBody, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(vehicleResponseBody);
    }

    @Test
    @WithMockUser
    @DisplayName("replace should return Bad Request when request body is invalid")
    void replace_ShouldReturnBadRequest_WhenDataIsNotValid() throws Exception {
        VehiclePutRequestBody invalidRequestBody = createInvalidVehiclePutRequestBody();

        mockMvc.perform(put("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
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

    private VehiclePostRequestBody createInvalidVehiclePostRequestBody() {
        return new VehiclePostRequestBody(
                "",
                "",
                1991L,
                "",
                "Volkswagen",
                5000.00
        );
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

    private VehiclePutRequestBody createInvalidVehiclePutRequestBody() {
        return new VehiclePutRequestBody(
                1L,
                "",
                "",
                2022L,
                "",
                "",
                25000.00
        );
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