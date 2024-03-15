package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.exception.BadRequestException;
import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.model.Vehicle;
import br.com.project.samuraicars.repositoy.ContactMessageRepository;
import br.com.project.samuraicars.repositoy.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void save(ContactMessageRequestBody requestBody) {
        contactMessageRepository.save(mapContactMessageRequestBodyToEntity(requestBody));
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetails userDetails) {
        ContactMessage message = findMessageById(id);
        if (userService.isUserOwnerOfResource(userDetails, findVehicleById(message.getVehicleId()))) {
            contactMessageRepository.delete(message);
        } else {
            throw new BadRequestException("Error deleting message");
        }
    }

    @Override
    public ContactMessageResponseBody listById(Long id, UserDetails userDetails) {
        ContactMessage message = findMessageById(id);
        if (userService.isUserOwnerOfResource(userDetails, findVehicleById(message.getVehicleId()))) {
            if (!message.isRead()) {
                markMessageAsRead(message);
            }
            return mapEntityToContactMessageResponseBody(message);
        }
        throw new BadRequestException("Bad Request, this user can't access this message");
    }

    @Override
    public Page<ContactMessageResponseBody> findByUserPageable(Pageable pageable, User user) {
        Page<ContactMessage> messagePage = contactMessageRepository.findAllByUser(user, pageable);
        return messagePage.map(this::mapEntityToContactMessageResponseBody);
    }

    private ContactMessage mapContactMessageRequestBodyToEntity(ContactMessageRequestBody requestBody) {
        Vehicle vehicle = findVehicleById(requestBody.vehicleId());
        ContactMessage message = new ContactMessage();
        message.setName(requestBody.name());
        message.setLastname(requestBody.lastname());
        message.setEmail(requestBody.email());
        message.setPhone(requestBody.phone());
        message.setMessage(requestBody.message());
        message.setRead(false);
        message.setVehicleId(requestBody.vehicleId());
        message.setUser(vehicle.getUser());
        return message;
    }

    private ContactMessageResponseBody mapEntityToContactMessageResponseBody(ContactMessage message) {
        return new ContactMessageResponseBody(
                message.getName(),
                message.getName(),
                message.getPhone(),
                message.getEmail(),
                message.getMessage(),
                message.getId(),
                message.getVehicleId(),
                message.getCreatedAt(),
                message.isRead()
        );
    }

    private ContactMessage findMessageById(Long id) {
        return contactMessageRepository.findById(id).orElseThrow(() -> new BadRequestException("Message not found"));
    }

    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new BadRequestException("Vehicle not found"));
    }

    @Transactional
    private void markMessageAsRead(ContactMessage message) {
        message.setRead(true);
        contactMessageRepository.save(message);
    }
}
