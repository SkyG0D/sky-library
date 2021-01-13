package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPutRequestBody;

@Mapper(componentModel = "spring")
public abstract class PublishingCompanyMapper {

    public static final PublishingCompanyMapper INSTANCE = Mappers.getMapper(PublishingCompanyMapper.class);

    public abstract PublishingCompany toPublishingCompany(
            PublishingCompanyPostRequestBody publishingCompanyPostRequestBody);

    public abstract PublishingCompany toPublishingCompany(
            PublishingCompanyPutRequestBody publishingCompanyPutRequestBody);

}
