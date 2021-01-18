package sky.skygod.skylibrary.util.user;

import sky.skygod.skylibrary.model.LibraryUser;

import java.util.UUID;

public class LibraryUserCreator {

    public static LibraryUser createLibraryUserToBeSaved() {
        return LibraryUser.builder()
            .name("Silvano Pimentel")
            .email("silvanin@hotmail.com")
            .password("{bcrypt}$2a$10$gP0E5f/RqncgOPn4o59KZuNQfm1LwOuRLp8u3ojTAaM6B8kFklRdC")
            .authorities("ROLE_USER, ROLE_ADMIN, ROLE_USER")
            .build();
    }

    public static LibraryUser createValidLibraryUser() {
        return LibraryUser.builder()
            .uuid(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"))
            .name("Silvano Pimentel")
            .email("silvanin@hotmail.com")
            .password("{bcrypt}$2a$10$gP0E5f/RqncgOPn4o59KZuNQfm1LwOuRLp8u3ojTAaM6B8kFklRdC")
            .authorities("ROLE_USER, ROLE_ADMIN, ROLE_USER")
            .build();
    }

    public static LibraryUser createValidUpdatedLibraryUser() {
        return LibraryUser.builder()
            .uuid(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"))
            .name("Silvano Marques")
            .email("silvanin@hotmail.com")
            .password("{bcrypt}$2a$10$gP0E5f/RqncgOPn4o59KZuNQfm1LwOuRLp8u3ojTAaM6B8kFklRdC")
            .authorities("ROLE_USER, ROLE_ADMIN, ROLE_USER")
            .build();
    }

}
