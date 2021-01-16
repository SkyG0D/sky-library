package sky.skygod.skylibrary.dto.user;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryUserPutRequestBody {

    @NotNull(message = "User uuid cannot be empty")
    private UUID uuid;

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email")
    @NotEmpty(message = "User email cannot be empty")
    private String email;

    @NotEmpty(message = "User password cannot be empty")
    private String password;

    @NotNull(message = "User authorities cannot be null")
    private List<String> authorities;

    private Address address;

}
