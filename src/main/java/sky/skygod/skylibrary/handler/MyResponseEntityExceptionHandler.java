package sky.skygod.skylibrary.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sky.skygod.skylibrary.exception.details.ExceptionDetails;
import sky.skygod.skylibrary.exception.details.ValidationExceptionDetails;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {

        Throwable throwable = Optional.ofNullable(ex.getCause())
                .orElseGet(() -> new Exception("An error has occurred"));
        String detailsMessage = Optional.ofNullable(ex.getMessage()).orElse("An error has occurred");

        ExceptionDetails details = ExceptionDetails.builder()
                .status(status.value())
                .title(throwable.getMessage())
                .details(detailsMessage)
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(details, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        field -> Optional.ofNullable(field.getDefaultMessage()).orElse("Field invalid")));

        ValidationExceptionDetails details = ValidationExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad request exception, invalid fields.")
                .details("Check the field(s) errors.")
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(details);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        Throwable throwable = Optional.ofNullable(ex.getCause())
                .orElseGet(() -> new Exception("An error has occurred"));
        String detailsMessage = Optional.ofNullable(ex.getMessage()).orElse("An error has occurred");

        ExceptionDetails details = ExceptionDetails.builder()
                .status(status.value())
                .title(throwable.getMessage())
                .details(detailsMessage)
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(details, headers, status);
    }

}
