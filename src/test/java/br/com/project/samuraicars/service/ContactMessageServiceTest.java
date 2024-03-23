package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.ContactMessageRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
class ContactMessageServiceTest {

    @Mock
    public ContactMessageRepository contactMessageRepository;
    @Mock
    public VehicleRepository vehicleRepository;
    @Mock
    public UserService userService;
    @InjectMocks
    public ContactMessageServiceImpl contactMessageService;

    private User validUser;
    private Vehicle validVehicle;
    private ContactMessage validContactMessage;
    private ContactMessageResponseBody validContactMessageResponseBody;

    @BeforeEach
    public void setUp() {
        validUser = createValidUser();
        validVehicle = createValidVehicle();
        validContactMessage = createValidContactMessage();
        validContactMessageResponseBody = createValidContactMessageResponseBody();

        BDDMockito.when(contactMessageRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(validContactMessage));
        BDDMockito.when(vehicleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(validVehicle));
    }

    @Test
    @DisplayName("save should not throw any exception when successful")
    void save_ShouldThrowNoException_WhenSuccessful() {
        BDDMockito.when(contactMessageRepository.save(ArgumentMatchers.any(ContactMessage.class))).thenReturn(validContactMessage);
        ContactMessageRequestBody requestBody = createValidContactMessageRequestBody();

        Assertions.assertThatCode(() -> contactMessageService.save(requestBody)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("save should not throw BadRequestException when Vehicle is not found")
    void save_ShouldThrowBadRequestException_WhenVehicleIsNotFound() {
        ContactMessageRequestBody invalidRequestBody = createInvalidContactMessageRequestBody();
        BDDMockito.when(vehicleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> contactMessageService.save(invalidRequestBody))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Vehicle not found");
    }

    @Test
    @DisplayName("delete should not throw any exception when successful")
    void delete_ShouldThrowNoException_WhenSuccessful() {
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);
        BDDMockito.doNothing().when(contactMessageRepository).delete(validContactMessage);

        Assertions.assertThatCode(() -> contactMessageService.delete(1L, validUser)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user does not have permission")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        User invalidUser = createInvalidUser();
        BDDMockito.when(userService.isUserAdmin(invalidUser)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(invalidUser, validVehicle)).thenReturn(false);
        BDDMockito.doNothing().when(contactMessageRepository).delete(ArgumentMatchers.any(ContactMessage.class));

        Assertions.assertThatCode(() -> contactMessageService.delete(1L, invalidUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Error deleting message");
    }

    @Test
    @DisplayName("listById should return ContactMessageResponseBody when successful")
    void listById_ShouldReturnContactMessageResponseBody_WhenSuccessful() {
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);

        ContactMessageResponseBody responseBody = contactMessageService.listById(1L, validUser);

        Assertions.assertThat(responseBody)
                .isNotNull()
                .isEqualTo(validContactMessageResponseBody);
    }

    @Test
    @DisplayName("listById should throw BadRequestException when the user does not have permission")
    void listById_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        User invalidUser = createInvalidUser();
        BDDMockito.when(userService.isUserAdmin(invalidUser)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(invalidUser, validVehicle)).thenReturn(false);

        Assertions.assertThatCode(() -> contactMessageService.listById(1L, invalidUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Bad Request, this user can't access this message");
    }

    @Test
    @DisplayName("listById should throw BadRequestException when message is not found")
    void listById_ShouldThrowBadRequestException_WhenMessageIsNotFound() {
        BDDMockito.when(userService.isUserOwnerOfResource(validUser, validVehicle)).thenReturn(true);
        BDDMockito.when(contactMessageRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> contactMessageService.listById(1L, validUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Message not found");
    }

    @Test
    @DisplayName("findByUserPageable should return a Page of ContactMessageResponseBody when successful")
    void findByUserPageable() {
        Page<ContactMessage> messagePage = new PageImpl<>(List.of(validContactMessage));
        BDDMockito.when(contactMessageRepository.findAllByUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(messagePage);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ContactMessageResponseBody> pageResponse = contactMessageService.findByUserPageable(pageRequest, validUser);

        Assertions.assertThat(pageResponse)
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageResponse.getContent().get(0))
                .isInstanceOf(ContactMessageResponseBody.class);
        Assertions.assertThat(pageResponse.getContent().get(0).isRead()).isFalse();
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

    private ContactMessage createValidContactMessage() {
        return new ContactMessage(
                1L,
                "Tom",
                "cat",
                "tomcat@email.com",
                "12345678910",
                "Hello, this is a nice car, so how can I buy it?",
                false,
                1L,
                validUser
        );
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

    private ContactMessageRequestBody createValidContactMessageRequestBody() {
        return new ContactMessageRequestBody(
                "Tom",
                "cat",
                "12345678910",
                "tomcat@email.com",
                "Hello, this is a nice car, so how can I buy it?",
                1L
        );
    }

    private ContactMessageRequestBody createInvalidContactMessageRequestBody() {
        return new ContactMessageRequestBody(
                "Tom",
                "cat",
                "12345678910",
                "tomcat@email.com",
                "Hello, this is a nice car, so how can I buy it?",
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