package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.ContactMessageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ContactMessageServiceImpl contactMessageService;
    @InjectMocks
    private ContactMessageController contactMessageController;

    private ContactMessageRequestBody contactMessageRequestBody;
    private ContactMessageResponseBody contactMessageResponseBody;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        contactMessageRequestBody = createValidContactMessageRequestBody();
        contactMessageResponseBody =  createValidContactMessageResponseBody();
        userDetails = createValidUser();
    }

    @Test
    @DisplayName("save should return status code Created when successful")
    void save_ShouldReturnStatusCodeCreated_WhenSuccessful() {
        BDDMockito.doNothing().when(contactMessageService).save(contactMessageRequestBody);

        ResponseEntity<Void> responseEntity = contactMessageController.save(contactMessageRequestBody);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("save should return Bad Request when data is invalid")
    void save_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        ContactMessageRequestBody invalidRequestBody = createInvalidContactMessageRequestBody();
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("displayById should return status code OK when successful")
    void displayById_ShouldReturnStatusCodeOk_WhenSuccessful() {
        BDDMockito.when(contactMessageService.listById(1L, userDetails)).thenReturn(contactMessageResponseBody);

        ResponseEntity<ContactMessageResponseBody> responseEntity = contactMessageController.displayById(1L, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(contactMessageResponseBody);
    }

    @Test
    @DisplayName("displayByUser should return status code OK when successful")
    void displayByUser_ShouldReturnStatusCodeOk_WhenSuccessful() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ContactMessageResponseBody> responseBodies = new PageImpl<>(List.of(contactMessageResponseBody));
        BDDMockito.when(contactMessageService.findByUserPageable((Pageable) pageRequest, (User) userDetails)).thenReturn(responseBodies);

        ResponseEntity<Page<ContactMessageResponseBody>> responseEntity = contactMessageController.displayByUser(pageRequest, userDetails);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody())
                .isNotEmpty()
                .contains(contactMessageResponseBody);
    }

    @Test
    @DisplayName("delete should return status code No Content when successful")
    void delete_ShouldReturnNoContent_WhenSuccessful() {
        BDDMockito.doNothing().when(contactMessageService).delete(1L, userDetails);

        ResponseEntity<Void> responseEntity = contactMessageController.delete(1L, userDetails);

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

    private ContactMessageRequestBody createValidContactMessageRequestBody() {
        return new ContactMessageRequestBody(
                "Tom",
                "cat",
                "12345678910",
                "tomcat@email.com",
                "Hello, this is a nice car, so how can I buy it?",
                0L
        );
    }

    private ContactMessageRequestBody createInvalidContactMessageRequestBody() {
        return new ContactMessageRequestBody(
                "",
                "",
                "",
                "",
                "",
                0L
        );
    }

    private ContactMessageResponseBody createValidContactMessageResponseBody() {
        return new ContactMessageResponseBody(
                "Tom",
                "cat",
                "12345678910",
                "tomcat@email.com",
                "Hello, this is a nice car, so how can I buy it?",
                1L,
                1L,
                null,
                true
        );
    }
}