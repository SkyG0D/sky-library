package sky.skygod.skylibrary.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Gender extends BaseEntity {

    @NotEmpty(message = "Gender name cannot be empty")
    private String name;

}


