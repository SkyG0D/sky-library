package sky.skygod.skylibrary.util.publishingcompany;

import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPutRequestBody;

public class PublishingCompanyPutRequestBodyCreator {

    public static PublishingCompanyPutRequestBody createPublishingCompanyPutRequestBodyToBeUpdate() {
        return PublishingCompanyPutRequestBody.builder()
            .uuid(PublishingCompanyCreator.createValidUpdatedPublishingCompany().getUuid())
            .name(PublishingCompanyCreator.createValidUpdatedPublishingCompany().getName())
            .build();
    }

}
