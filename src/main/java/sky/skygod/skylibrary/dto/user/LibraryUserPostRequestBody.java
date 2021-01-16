package sky.skygod.skylibrary.dto.user;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryUserPostRequestBody {

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email")
    @NotEmpty(message = "Email name cannot be empty")
    private String email;

    @NotEmpty(message = "Password name cannot be empty")
    private String password;

    @NotNull(message = "Authorities cannot be null")
    private Set<String> authorities;

    private Address address;

}
