package sky.skygod.skylibrary.requests.book;

import lombok.*;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.model.Status;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPostRequestBody {

    @NotEmpty(message = "Book name cannot be empty")
    private String name;

    @NotNull(message = "Book pages cannot be null")
    @Size(min = 10, message = "Book must have at least 10 pages")
    private Integer pages;

    @NotNull(message = "Book status cannot be null")
    private Status status;

    @NotNull(message = "Book authors cannot be null")
    private Set<Author> authors;

    @NotNull(message = "Book genders cannot be null")
    private Set<Gender> genders;

    @NotNull(message = "Book publishing company cannot be null")
    private PublishingCompany publishingCompany;

}
