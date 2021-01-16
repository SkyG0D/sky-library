package sky.skygod.skylibrary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotExistsException extends RuntimeException {

    private final Set<String> roles;

    public RoleNotExistsException(Set<String> roles, String message) {
        super(message);
        this.roles = roles;
    }

    public RoleNotExistsException(Set<String> roles, String message, Throwable cause) {
        super(message, cause);
        this.roles = roles;
    }

}
