package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.requests.book.BookPostRequestBody;
import sky.skygod.skylibrary.requests.book.BookPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    public static final BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    public abstract Book toBook(BookPostRequestBody bookPostRequestBody);

    public abstract Book toBook(BookPutRequestBody bookPutRequestBody);

}
