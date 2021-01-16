package sky.skygod.skylibrary.dto.user;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryUserGetResponseBody {

    private UUID uuid;
    private LocalDateTime createdAt;
    private String name;
    private Address address;

}
