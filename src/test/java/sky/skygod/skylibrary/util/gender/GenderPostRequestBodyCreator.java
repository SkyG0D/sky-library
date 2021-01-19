package sky.skygod.skylibrary.util.gender;

import sky.skygod.skylibrary.dto.gender.GenderPostRequestBody;

public class GenderPostRequestBodyCreator {

    public static GenderPostRequestBody createGenderPostRequestBodyToBeSave() {
        return GenderPostRequestBody
            .builder()
            .name(GenderCreator.createValidGender().getName())
            .build();
    }

}
