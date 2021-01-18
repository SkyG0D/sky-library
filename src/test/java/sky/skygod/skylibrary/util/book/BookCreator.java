package sky.skygod.skylibrary.util.book;

import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.model.Status;
import sky.skygod.skylibrary.util.author.AuthorCreator;
import sky.skygod.skylibrary.util.gender.GenderCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BookCreator {

    public static Book createBookToBeSaved() {
        return Book.builder()
            .name("Estrutura de dados")
            .description("Livro sobre estrutura de dados e algoritmos")
            .pages(150)
            .isbn(1L)
            .status(Status.AVAILABLE)
            .authors(new HashSet<>(Set.of(AuthorCreator.createValidAuthor())))
            .genders(new HashSet<>(Set.of(GenderCreator.createValidGender())))
            .publishingCompany(PublishingCompanyCreator.createValidPublishingCompany())
            .build();
    }

    public static Book createValidBook() {
        return Book.builder()
            .uuid(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"))
            .name("Estrutura de dados")
            .description("Livro sobre estrutura de dados e algoritmos")
            .pages(150)
            .isbn(1L)
            .status(Status.AVAILABLE)
            .authors(new HashSet<>(Set.of(AuthorCreator.createValidAuthor())))
            .genders(new HashSet<>(Set.of(GenderCreator.createValidGender())))
            .publishingCompany(PublishingCompanyCreator.createValidPublishingCompany())
            .build();
    }

    public static Book createValidUpdatedBook() {
        return Book.builder()
            .uuid(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"))
            .name("Estrutura de dados com JS")
            .description("Livro sobre estrutura de dados e algoritmos")
            .pages(150)
            .isbn(1L)
            .status(Status.UNAVAILABLE)
            .authors(new HashSet<>(Set.of(AuthorCreator.createValidAuthor())))
            .genders(new HashSet<>(Set.of(GenderCreator.createValidGender())))
            .publishingCompany(PublishingCompanyCreator.createValidPublishingCompany())
            .build();
    }

}
