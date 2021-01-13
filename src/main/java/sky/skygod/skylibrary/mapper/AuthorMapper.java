package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.requests.author.AuthorPostRequestBody;
import sky.skygod.skylibrary.requests.author.AuthorPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    public static final AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    public abstract Author toAuthor(AuthorPostRequestBody authorPostRequestBody);

    public abstract Author toAuthor(AuthorPutRequestBody authorPutRequestBody);

}
