package sky.skygod.skylibrary.requests.publishingcompany;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishingCompanyPutRequestBody {

    @NotNull(message = "Publishing company uuid cannot be null")
    private UUID uuid;

    @NotEmpty(message = "Publishing company name cannot be empty")
    private String name;

    private Address address;

}
