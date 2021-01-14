package sky.skygod.skylibrary.repository.book;

import lombok.Getter;
import lombok.Setter;
import sky.skygod.skylibrary.model.Status;

@Getter
@Setter
public class BookFilter {

    private String name;

    private String description;

    private Status status;

}
