package sky.skygod.skylibrary.util.publishingcompany;

import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPostRequestBody;

public class PublishingCompanyPostRequestBodyCreator {

    public static PublishingCompanyPostRequestBody createPublishingCompanyPostRequestBodyToBeSaved() {
        return PublishingCompanyPostRequestBody.builder()
            .name(PublishingCompanyCreator.createPublishingCompanyToBeSaved().getName())
            .build();
    }

}
