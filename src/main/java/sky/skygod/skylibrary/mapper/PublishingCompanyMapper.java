package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPutRequestBody;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PublishingCompanyMapper {

    public static final PublishingCompanyMapper INSTANCE = Mappers.getMapper(PublishingCompanyMapper.class);

    public abstract PublishingCompany toPublishingCompany(
            PublishingCompanyPostRequestBody publishingCompanyPostRequestBody);

    public abstract PublishingCompany toPublishingCompany(
            PublishingCompanyPutRequestBody publishingCompanyPutRequestBody);

}
