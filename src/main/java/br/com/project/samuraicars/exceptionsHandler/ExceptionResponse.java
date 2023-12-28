package br.com.project.samuraicars.exceptionsHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(
        LocalDateTime timestamp,
        String status,
        String title,
        String details,
        @JsonProperty("developer_message")
        String developerMessage
) {
}
