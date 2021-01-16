package sky.skygod.skylibrary.exception.details;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class EmailExistsExceptionDetails extends ExceptionDetails {

    private final String email;

}
