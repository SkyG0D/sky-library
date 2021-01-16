package sky.skygod.skylibrary.exception.details;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@SuperBuilder
public class UnauthorizedExceptionDetails extends ExceptionDetails {

    private final Set<String> userRoles;

}
