package sky.skygod.skylibrary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;

}
