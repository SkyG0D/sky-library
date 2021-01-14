package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.dto.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class GenderMapper {

    public static final GenderMapper INSTANCE = Mappers.getMapper(GenderMapper.class);

    public abstract Gender toGender(GenderPostRequestBody genderPostRequestBody);

    public abstract Gender toGender(GenderPutRequestBody genderPutRequestBody);

}
