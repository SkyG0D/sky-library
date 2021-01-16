package sky.skygod.skylibrary.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO: Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not
    //  supported]
    // TODO: https://stackoverflow.com/questions/56384933/spring-method-not-allowed-returns-error-code-403-forbidden
    //  -instead-of-405

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FileSizeLimitExceededExceptionDetails handleFileSizeLimitExceededException(
            IllegalStateException ex) {

        FileSizeLimitExceededExceptionDetails details = FileSizeLimitExceededExceptionDetails
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("File size exceeded, upload a smaller file.")
                .timestamp(LocalDateTime.now())
                .build();

        try {
            FileSizeLimitExceededException fileExceeded = (FileSizeLimitExceededException) ex.getCause();
            details.setDetails(fileExceeded.getMessage());
            details.setDeveloperMessage(fileExceeded.getClass().getName());
        } catch (Exception _ex) {
            details.setDetails(ex.getMessage());
            details.setDeveloperMessage(ex.getClass().getName());
        }

        return details;
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FileStorageExceptionDetails handleFileStorageException(FileStorageException ex) {
        return FileStorageExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("File storage exception, check the documentation.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MyFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyFileNotFoundExceptionDetails handleMyFileNotFoundException(MyFileNotFoundException ex) {
        return MyFileNotFoundExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("File not found exception, check file name.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundExceptionDetails handleNotFoundException(NotFoundException ex) {
        return NotFoundExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Not found exception, check the documentation.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
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
