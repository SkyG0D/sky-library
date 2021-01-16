package sky.skygod.skylibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleNotExistsException extends RuntimeException {

    public RoleNotExistsException(String message) {
        super(message);
    }

    public RoleNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
