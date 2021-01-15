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
import sky.skygod.skylibrary.exception.FileStorageException;
import sky.skygod.skylibrary.exception.MyFileNotFoundException;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.exception.details.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO: Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported]
    // TODO: https://stackoverflow.com/questions/56384933/spring-method-not-allowed-returns-error-code-403-forbidden-instead-of-405

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<FileStorageExceptionDetails> handleFileStorageException(FileStorageException ex) {
        FileStorageExceptionDetails fileStorageExceptionDetails = FileStorageExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("File storage exception, check the documentation.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(fileStorageExceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyFileNotFoundException.class)
    public ResponseEntity<MyFileNotFoundExceptionDetails> handleMyFileNotFoundException(MyFileNotFoundException ex) {
        MyFileNotFoundExceptionDetails myFileNotFoundExceptionDetails = MyFileNotFoundExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("File not found exception, check file name.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(myFileNotFoundExceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundExceptionDetails> handleNotFoundException(NotFoundException ex) {
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
