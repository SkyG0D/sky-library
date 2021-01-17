package sky.skygod.skylibrary.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import sky.skygod.skylibrary.exception.*;
import sky.skygod.skylibrary.exception.details.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDetails handleAllException(Exception ex) {
        return ExceptionDetails.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("An internal error has occurred")
                .details(Optional.ofNullable(ex.getMessage()).orElse("Internal server error"))
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDetails handleAllRuntimeException(RuntimeException ex) {
        return ExceptionDetails.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("An internal error has occurred")
                .details(Optional.ofNullable(ex.getMessage()).orElse("Internal server error"))
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnauthorizedExceptionDetails handleUnauthorizedException(UnauthorizedException ex) {
        return UnauthorizedExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("This user does not have sufficient permission")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .userRoles(ex.getUserRoles())
                .build();
    }

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmailExistsExceptionDetails handleEmailExistsException(EmailExistsException ex) {
        return EmailExistsExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("This email already exists")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .email(ex.getEmail())
                .build();
    }

    @ExceptionHandler(RoleNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RoleNotExistsExceptionDetails handleRoleNotExistsException(RoleNotExistsException ex) {
        final List<String> EXISTING_ROLES = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER");
        Set<String> nonExistentRoles = ex.getRoles().stream()
                .filter(role -> !EXISTING_ROLES.contains(role))
                .collect(Collectors.toSet());

        return RoleNotExistsExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Role not found, check the documentation to view all roles.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .roles(nonExistentRoles)
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public FileSizeLimitExceededExceptionDetails handleSizeLimitExceededException(
            MaxUploadSizeExceededException ex) {
        return FileSizeLimitExceededExceptionDetails
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("File size exceeded, upload a smaller file.")
                .details(ex.getMostSpecificCause().getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public NotFoundExceptionDetails handleNotFoundException(NotFoundException ex) {
        return NotFoundExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("Not found exception, check the documentation.")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
