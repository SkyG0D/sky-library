package sky.skygod.skylibrary.dto.book;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.model.Status;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPutRequestBody {

    @NotNull(message = "Book uuid cannot be null")
    @EqualsAndHashCode.Include()
    private UUID uuid;

    @NotEmpty(message = "Book name cannot be empty")
    private String name;

    @NotNull(message = "Book pages cannot be null")
    @Range(min = 10, message = "Book must have at least 10 pages")
    private Integer pages;

    @NotNull(message = "Book status cannot be null")
    private Status status;

    @NotNull(message = "Book authors cannot be null")
    private Set<Author> authors;

    @NotNull(message = "Book genders cannot be null")
    private Set<Gender> genders;

    @NotNull(message = "Book publishing company cannot be null")
    private PublishingCompany publishingCompany;

    private Long isbn;
    private String cover;
    private String description;

}
