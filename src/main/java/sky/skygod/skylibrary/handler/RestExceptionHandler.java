package sky.skygod.skylibrary.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sky.skygod.skylibrary.exception.ExceptionDetails;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.exception.NotFoundExceptionDetails;
import sky.skygod.skylibrary.exception.ValidationExceptionDetails;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundExceptionDetails> handleNotFoundExceptionDetails(NotFoundException ex) {
        NotFoundExceptionDetails notFoundExceptionDetails = NotFoundExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Not found exception, check the documentation.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(notFoundExceptionDetails, HttpStatus.NOT_FOUND);
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

        ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad request exception, invalid fields.")
                .details("Check the field(s) errors.")
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(validationExceptionDetails);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        Throwable throwable = Optional.ofNullable(ex.getCause())
                .orElseGet(() -> new Exception("An error has occurred"));
        String details = Optional.ofNullable(ex.getMessage()).orElse("An error has occurred");

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .status(status.value())
                .title(throwable.getMessage())
                .details(details)
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, headers, status);
    }
}
