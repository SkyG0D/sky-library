package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.dto.author.AuthorPostRequestBody;
import sky.skygod.skylibrary.dto.author.AuthorPutRequestBody;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AuthorMapper {

    public static final AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    public abstract Author toAuthor(AuthorPostRequestBody authorPostRequestBody);

    public abstract Author toAuthor(AuthorPutRequestBody authorPutRequestBody);

}
