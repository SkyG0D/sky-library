package sky.skygod.skylibrary.requests.gender;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenderPostRequestBody {

    @NotEmpty(message = "Gender name cannot be empty")
    private String name;

}
