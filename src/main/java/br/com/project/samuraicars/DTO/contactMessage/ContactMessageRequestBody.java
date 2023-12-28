package br.com.project.samuraicars.DTO.contactMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record ContactMessageRequestBody(
        @NotBlank()
        @Size(min = 2, max = 20)
        String name,
        @NotBlank()
        @Size(min = 2, max = 20)
        String lastname,
        @Pattern(regexp = "\\d{11}", message = "Invalid phone number")
        String phone,
        @Email()
        @NotBlank()
        String email,
        @NotBlank()
        @Size(min = 10, max = 500)
        String message,
        @JsonProperty("vehicle_id")
        @NotNull()
        Long vehicleId
) {
}
