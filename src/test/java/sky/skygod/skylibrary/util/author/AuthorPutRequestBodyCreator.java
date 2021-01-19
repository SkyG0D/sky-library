package sky.skygod.skylibrary.util.author;

import sky.skygod.skylibrary.dto.author.AuthorPutRequestBody;

public class AuthorPutRequestBodyCreator {

    public static AuthorPutRequestBody createAuthorPutRequestBodyToBeUpdate() {
        return AuthorPutRequestBody.builder()
            .uuid(AuthorCreator.createValidUpdatedAuthor().getUuid())
            .name(AuthorCreator.createValidUpdatedAuthor().getName())
            .dateOfBirth(AuthorCreator.createValidUpdatedAuthor().getDateOfBirth())
            .build();
    }

}
