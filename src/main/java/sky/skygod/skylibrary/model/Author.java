package sky.skygod.skylibrary.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Author extends BaseEntity {

    @NotEmpty(message = "Author name cannot be empty")
    private String name;

    @NotNull(message = "Author date of birth cannot be null")
    private LocalDate dateOfBirth;

    @Embedded
    private Address address;

}


