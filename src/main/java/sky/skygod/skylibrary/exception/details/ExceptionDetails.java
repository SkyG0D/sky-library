package sky.skygod.skylibrary.exception.details;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {

    protected int status;
    protected String title;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;

}
