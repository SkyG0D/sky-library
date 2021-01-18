package sky.skygod.skylibrary.util.publishingcompany;

import sky.skygod.skylibrary.model.PublishingCompany;

import java.util.UUID;

public class PublishingCompanyCreator {

    public static PublishingCompany createPublishingCompanyToBeSaved() {
        return PublishingCompany.builder()
            .name("SkyBooks")
            .build();
    }

    public static PublishingCompany createValidPublishingCompany() {
        return PublishingCompany.builder()
            .uuid(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
            .name("SkyBooks")
            .build();
    }

    public static PublishingCompany createValidUpdatedPublishingCompany() {
        return PublishingCompany.builder()
            .uuid(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
            .name("Atlas SkyBooks")
            .build();
    }

}
