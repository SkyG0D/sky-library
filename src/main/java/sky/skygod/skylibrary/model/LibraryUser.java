package sky.skygod.skylibrary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class LibraryUser extends BaseEntity {

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email")
    @NotEmpty(message = "Email name cannot be empty")
    private String email;

    @NotEmpty(message = "User password cannot be empty")
    private String password;

    @NotEmpty(message = "User authorities cannot be empty")
    private String authorities;

    @Embedded
    private Address address;

}
