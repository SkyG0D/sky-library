package sky.skygod.skylibrary.repository.book;

import lombok.Getter;
import lombok.Setter;
import sky.skygod.skylibrary.model.Status;

import java.util.UUID;

@Getter
@Setter
public class BookFilter {

    private String name;

    private String description;

    private Status status;

    private UUID authorId;

    private String authorName;

    private UUID genderId;

    private String genderName;

    private UUID publishingCompanyId;

    private String publishingCompanyName;

}
