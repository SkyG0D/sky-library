package sky.skygod.skylibrary.util.gender;

import sky.skygod.skylibrary.model.Gender;

import java.util.UUID;

public class GenderCreator {

    public static Gender createGenderToBeSaved() {
        return Gender.builder()
            .name("Aventura")
            .build();
    }

    public static Gender createValidGender() {
        return Gender.builder()
            .uuid(UUID.fromString("1332d988-f8cc-49e4-874f-4772e0db7023"))
            .name("Aventura")
            .build();
    }

    public static Gender createValidUpdatedGender() {
        return Gender.builder()
            .uuid(UUID.fromString("1332d988-f8cc-49e4-874f-4772e0db7023"))
            .name("Ação")
            .build();
    }

}
