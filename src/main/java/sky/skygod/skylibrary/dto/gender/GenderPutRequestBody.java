package sky.skygod.skylibrary.dto.gender;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenderPutRequestBody {

    @NotNull(message = "Gender uuid cannot be empty")
    private UUID uuid;

    @NotEmpty(message = "Gender name cannot be empty")
    private String name;

}
