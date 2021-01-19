package sky.skygod.skylibrary.util.book;

import sky.skygod.skylibrary.dto.book.BookPostRequestBody;

public class BookPostRequestBodyCreator {

    public static BookPostRequestBody createBookPostRequestBodyToBeSave() {
        return BookPostRequestBody.builder()
            .name(BookCreator.createValidBook().getName())
            .pages(BookCreator.createValidBook().getPages())
            .status(BookCreator.createValidBook().getStatus())
            .authors(BookCreator.createValidBook().getAuthors())
            .genders(BookCreator.createValidBook().getGenders())
            .publishingCompany(BookCreator.createValidBook().getPublishingCompany())
            .isbn(BookCreator.createValidBook().getIsbn())
            .cover(BookCreator.createValidBook().getCover())
            .description(BookCreator.createValidBook().getDescription())
            .build();
    }

}
