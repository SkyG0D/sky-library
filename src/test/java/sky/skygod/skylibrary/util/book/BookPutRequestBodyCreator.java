package sky.skygod.skylibrary.util.book;

import sky.skygod.skylibrary.dto.book.BookPutRequestBody;

public class BookPutRequestBodyCreator {

    public static BookPutRequestBody createBookPutRequestBodyToBeUpdate() {
        return BookPutRequestBody.builder()
            .uuid(BookCreator.createValidUpdatedBook().getUuid())
            .name(BookCreator.createValidUpdatedBook().getName())
            .pages(BookCreator.createValidUpdatedBook().getPages())
            .status(BookCreator.createValidUpdatedBook().getStatus())
            .authors(BookCreator.createValidUpdatedBook().getAuthors())
            .genders(BookCreator.createValidUpdatedBook().getGenders())
            .publishingCompany(BookCreator.createValidUpdatedBook().getPublishingCompany())
            .isbn(BookCreator.createValidUpdatedBook().getIsbn())
            .cover(BookCreator.createValidUpdatedBook().getCover())
            .description(BookCreator.createValidUpdatedBook().getDescription())
            .build();
    }

}
