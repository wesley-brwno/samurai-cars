package br.com.project.samuraicars.exceptionsHandler;

import br.com.project.samuraicars.exception.EmailAlreadyExistException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                ValidationExceptionResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .title("Invalid arguments, check the documentation!")
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build()
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        return ResponseEntity.status(statusCode.value()).body(createExceptionResponse(ex, HttpStatus.valueOf(statusCode.value()), ex.getCause().getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createExceptionResponse(ex, HttpStatus.FORBIDDEN, "Bad Credentials, check email and password!"));
    }


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(createExceptionResponse(ex, HttpStatus.CONFLICT, "Email already exist!"));
    }

    private ExceptionResponse createExceptionResponse(Exception exception, HttpStatus status, String title) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.name())
                .details(exception.getMessage())
                .build();
    }
}
