package br.com.project.samuraicars.exceptionsHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ValidationExceptionResponse(
        LocalDateTime timestamp,
        String status,
        String title,
        String details,
        String fields,
        @JsonProperty("fields_message")
        String fieldsMessage
) {
}
