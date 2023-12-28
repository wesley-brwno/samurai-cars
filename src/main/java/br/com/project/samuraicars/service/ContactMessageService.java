package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final UserService userService;
    private final VehicleService vehicleService;

    public ContactMessage save(ContactMessageRequestBody messageBody) {
        Vehicle vehicle = vehicleService.findById(messageBody.vehicleId());
        User user = userService.findById(vehicle.getUser().getId());
        return contactMessageRepository.save(new ContactMessage(null, messageBody.name(), messageBody.lastname(), messageBody.email(),
                messageBody.phone(), messageBody.message(), false, messageBody.vehicleId(), user));
    }

    public ContactMessageResponseBody getById(Long id, UserDetails user) {
        ContactMessage message = findById(id);
        if (isUserOwnerOfMessage(message.getUser().getId(), user)) {
            markAsRead(message);
            return new ContactMessageResponseBody(message);
        }
        return null;
    }

    private ContactMessage findById(Long id) {
        return contactMessageRepository.findById(id).orElseThrow(() -> new BadRequestException("Message not found"));
    }

    public void delete(Long id, UserDetails user) {
        ContactMessage message = findById(id);
        Vehicle vehicle = vehicleService.findById(message.getVehicleId());
        if (userService.isUserOwnerOfResource(user, vehicle)) {
            contactMessageRepository.delete(message);
            return;
        }
        throw new BadRequestException("Error deleting message");
    }

    public Page<ContactMessageResponseBody> findByUserPageable(Pageable pageable, UserDetails user) {
        Page<ContactMessage> messagesPage = contactMessageRepository.findAllByUser((User) user, pageable);
        return messagesPage.map(ContactMessageResponseBody::new);
    }

    private boolean isUserOwnerOfMessage(Long userId, UserDetails userDetails) {
        User user = userService.findById(userId);
        if (user.getUsername().equals(userDetails.getUsername())) {
            return true;
        }
        throw new BadRequestException("Bad Request, this user can't access this message");
    }

    private void markAsRead(ContactMessage contactMessage) {
        contactMessage.setRead(true);
        contactMessageRepository.save(contactMessage);
    }
}
