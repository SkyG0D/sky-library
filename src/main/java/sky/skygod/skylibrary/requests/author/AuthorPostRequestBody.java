package sky.skygod.skylibrary.requests.author;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorPostRequestBody {

    @NotEmpty(message = "Author name cannot be empty")
    private String name;

    @NotNull(message = "Author date of birth cannot be null")
    private LocalDate dateOfBirth;

    private Address address;

}
