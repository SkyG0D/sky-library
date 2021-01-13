package sky.skygod.skylibrary.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class PublishingCompany extends BaseEntity {

    @NotEmpty(message = "Publishing company name cannot be empty")
    private String name;

    @Embedded
    private Address address;

}
