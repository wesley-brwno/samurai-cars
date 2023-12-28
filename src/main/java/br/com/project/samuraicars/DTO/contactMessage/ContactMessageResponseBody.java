package br.com.project.samuraicars.DTO.contactMessage;

import br.com.project.samuraicars.model.ContactMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ContactMessageResponseBody(
        String name,
        String lastName,
        String phone,
        String email,
        String message,
        Long id,
        @JsonProperty("vehicle_id")
        Long vehicleId,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("is_read")
        boolean isRead
) {
        public ContactMessageResponseBody(String name, String lastName, String phone, String email, String message, Long id, @JsonProperty("vehicle_id")
        Long vehicleId, @JsonProperty("created_at")
                                          LocalDateTime createdAt, @JsonProperty("is_read")
                                          boolean isRead) {
                this.name = name;
                this.lastName = lastName;
                this.phone = phone;
                this.email = email;
                this.message = message;
                this.id = id;
                this.vehicleId = vehicleId;
                this.createdAt = createdAt;
                this.isRead = isRead;
        }

        public ContactMessageResponseBody(ContactMessage message) {
                this(
                        message.getName(),
                        message.getLastname(),
                        message.getPhone(),
                        message.getEmail(),
                        message.getMessage(),
                        message.getId(),
                        message.getVehicleId(),
                        message.getCreatedAt(),
                        message.isRead()
                );
        }
}
