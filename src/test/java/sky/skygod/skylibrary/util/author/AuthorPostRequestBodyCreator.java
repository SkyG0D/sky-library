package sky.skygod.skylibrary.util.author;

import sky.skygod.skylibrary.dto.author.AuthorPostRequestBody;

public class AuthorPostRequestBodyCreator {

    public static AuthorPostRequestBody createAuthorPostRequestBodyToBeSave() {
        return AuthorPostRequestBody.builder()
            .name(AuthorCreator.createValidAuthor().getName())
            .dateOfBirth(AuthorCreator.createValidAuthor().getDateOfBirth())
            .build();
    }

}
