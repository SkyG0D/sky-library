package sky.skygod.skylibrary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistsException extends RuntimeException {

    private final String email;

    public EmailExistsException(String email, String message) {
        super(message);
        this.email = email;
    }

    public EmailExistsException(String email, String message, Throwable cause) {
        super(message, cause);
        this.email = email;
    }

}
