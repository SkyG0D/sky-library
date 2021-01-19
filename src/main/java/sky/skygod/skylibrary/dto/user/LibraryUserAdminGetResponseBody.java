package sky.skygod.skylibrary.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sky.skygod.skylibrary.model.Address;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryUserAdminGetResponseBody {

    private UUID uuid;
    private LocalDateTime createdAt;
    private String name;
    private String email;
    private Set<String> authorities;
    private Address address;

}
