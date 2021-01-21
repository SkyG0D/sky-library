package sky.skygod.skylibrary.dto.user;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryUserAdminGetResponseBody {

    @EqualsAndHashCode.Include()
    private UUID uuid;

    private LocalDateTime createdAt;
    private String name;
    private String email;
    private Set<String> authorities;
    private Address address;

}
