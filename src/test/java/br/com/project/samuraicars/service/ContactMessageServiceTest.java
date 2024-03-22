package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.ContactMessageRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import br.com.project.samuraicars.utils.ContactMessageCreator;
import br.com.project.samuraicars.utils.ContactMessageRequestBodyCreator;
import br.com.project.samuraicars.utils.UserCreator;
import br.com.project.samuraicars.utils.VehicleCreator;
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

    @BeforeEach
    public void setUp() {
        ContactMessage contactMessage = ContactMessageCreator.createValidContactMessage();
        Vehicle vehicle = VehicleCreator.createValidVehicle();

        BDDMockito.when(contactMessageRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(contactMessage));
        BDDMockito.when(vehicleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(vehicle));
    }

    @Test
    @DisplayName("save should not throw any exception when successful")
    void save_ShouldThrowNoException_WhenSuccessful() {
        ContactMessage contactMessage = ContactMessageCreator.createValidContactMessage();
        BDDMockito.when(contactMessageRepository.save(ArgumentMatchers.any(ContactMessage.class))).thenReturn(contactMessage);
        ContactMessageRequestBody requestBody = ContactMessageRequestBodyCreator.createValidContactMessageRequestBody();

        Assertions.assertThatCode(() -> contactMessageService.save(requestBody)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should not throw any exception when successful")
    void delete_ShouldThrowNoException_WhenSuccessful() {
        User user = UserCreator.createValidUser();
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        BDDMockito.when(userService.isUserAdmin(user)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(user, vehicle)).thenReturn(true);
        BDDMockito.doNothing().when(contactMessageRepository).delete(ArgumentMatchers.any(ContactMessage.class));

        Assertions.assertThatCode(() -> contactMessageService.delete(1L, user)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete should throw BadRequestException when user does not have permission")
    void delete_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        User user = UserCreator.createValidUser();
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        BDDMockito.when(userService.isUserAdmin(user)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(user, vehicle)).thenReturn(false);
        BDDMockito.doNothing().when(contactMessageRepository).delete(ArgumentMatchers.any(ContactMessage.class));

        Assertions.assertThatCode(() -> contactMessageService.delete(1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Error deleting message");
    }

    @Test
    @DisplayName("listById should return ContactMessageResponseBody when successful")
    void listById_ShouldReturnContactMessageResponseBody_WhenSuccessful() {
        ContactMessage contactMessage = ContactMessageCreator.createValidContactMessage();
        User user = UserCreator.createValidUser();
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        BDDMockito.when(userService.isUserAdmin(user)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(user, vehicle)).thenReturn(true);

        ContactMessageResponseBody responseBody = contactMessageService.listById(1L, user);

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.id()).isEqualTo(contactMessage.getId());
        Assertions.assertThat(responseBody.isRead()).isTrue();
    }

    @Test
    @DisplayName("listById should throw BadRequestException when the user does not have permission")
    void listById_ShouldThrowBadRequestException_WhenUserHasNoPermission() {
        User user = UserCreator.createValidUser();
        Vehicle vehicle = VehicleCreator.createValidVehicle();
        BDDMockito.when(userService.isUserAdmin(user)).thenReturn(false);
        BDDMockito.when(userService.isUserOwnerOfResource(user, vehicle)).thenReturn(false);

        Assertions.assertThatCode(() -> contactMessageService.listById(1L, user))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Bad Request, this user can't access this message");
    }

    @Test
    @DisplayName("findByUserPageable should return a Page of ContactMessageResponseBody when successful")
    void findByUserPageable() {
        ContactMessage contactMessage = ContactMessageCreator.createValidContactMessage();
        User user = UserCreator.createValidUser();
        Page<ContactMessage> messagePage = new PageImpl<>(List.of(contactMessage));
        BDDMockito.when(contactMessageRepository.findAllByUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(messagePage);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ContactMessageResponseBody> pageResponse = contactMessageService.findByUserPageable(pageRequest, user);

        Assertions.assertThat(pageResponse)
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageResponse.getContent().get(0))
                .isInstanceOf(ContactMessageResponseBody.class);
    }
}