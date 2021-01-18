package sky.skygod.skylibrary.util.author;

import sky.skygod.skylibrary.model.Author;

import java.time.LocalDate;
import java.util.UUID;

public class AuthorCreator {

    public static Author createAuthorToBeSaved() {
        return Author.builder()
            .name("Silvano Pimentel")
            .dateOfBirth(LocalDate.parse("2004-09-04"))
            .build();
    }

    public static Author createValidAuthor() {
        return Author.builder()
            .uuid(UUID.fromString("76389962-ca1b-453b-99f3-bef31036f673"))
            .name("Silvano Pimentel")
            .dateOfBirth(LocalDate.parse("2004-09-04"))
            .build();
    }

    public static Author createValidUpdatedAuthor() {
        return Author.builder()
            .uuid(UUID.fromString("76389962-ca1b-453b-99f3-bef31036f673"))
            .name("Silvano Marques")
            .dateOfBirth(LocalDate.parse("2004-09-04"))
            .build();
    }

}
