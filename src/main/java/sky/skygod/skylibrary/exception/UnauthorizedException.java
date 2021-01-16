package sky.skygod.skylibrary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnauthorizedException extends RuntimeException {

    private final Set<String> userRoles;

    public UnauthorizedException(Set<String> userRoles, String message) {
        super(message);
        this.userRoles = userRoles;
    }

    public UnauthorizedException(Set<String> userRoles, String message, Throwable cause) {
        super(message, cause);
        this.userRoles = userRoles;
    }

}
