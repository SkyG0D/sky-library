package sky.skygod.skylibrary.dto.author;

import lombok.*;
import sky.skygod.skylibrary.model.Address;
import sky.skygod.skylibrary.model.Book;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorPutRequestBody {

    @NotNull(message = "Author uuid cannot be empty")
    private UUID uuid;

    @NotEmpty(message = "Author name cannot be empty")
    private String name;

    @NotNull(message = "Author date of birth cannot be null")
    private LocalDate dateOfBirth;

    private Address address;
}
