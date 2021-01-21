package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.dto.book.BookGetResumedResponseBody;
import sky.skygod.skylibrary.dto.book.BookPostRequestBody;
import sky.skygod.skylibrary.dto.book.BookPutRequestBody;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BookMapper {

    public static final BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    public abstract Book toBook(BookPostRequestBody bookPostRequestBody);

    public abstract Book toBook(BookPutRequestBody bookPutRequestBody);

    public abstract BookGetResumedResponseBody toBookGetResumedResponseBody(Book book);

    Set<String> mapAuthors(Set<Author> source) {
        return source.stream().map(Author::getName).collect(Collectors.toSet());
    }

    Set<String> mapGenders(Set<Gender> source) {
        return source.stream().map(Gender::getName).collect(Collectors.toSet());
    }

    String mapPublishingCompany(PublishingCompany source) {
        return source.getName();
    }

}
