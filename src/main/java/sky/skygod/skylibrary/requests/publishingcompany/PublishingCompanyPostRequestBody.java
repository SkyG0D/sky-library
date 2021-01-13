package sky.skygod.skylibrary.requests.publishingcompany;

import lombok.*;
import sky.skygod.skylibrary.model.Address;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishingCompanyPostRequestBody {

    @NotEmpty(message = "Publishing company name cannot be empty")
    private String name;

    private Address address;

}
