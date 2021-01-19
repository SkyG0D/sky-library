package sky.skygod.skylibrary.util.gender;

import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;

public class GenderPutRequestBodyCreator {

    public static GenderPutRequestBody createGenderPutRequestBodyToBeUpdate() {
        return GenderPutRequestBody.builder()
            .uuid(GenderCreator.createValidUpdatedGender().getUuid())
            .name(GenderCreator.createValidUpdatedGender().getName())
            .build();
    }

}
