package sky.skygod.skylibrary.exception.details;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@SuperBuilder
public class RoleNotExistsExceptionDetails extends ExceptionDetails {

    private final Set<String> roles;

}
