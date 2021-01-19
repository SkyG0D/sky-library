package sky.skygod.skylibrary.util.user;

import org.springframework.security.core.GrantedAuthority;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;

import java.util.Set;
import java.util.stream.Collectors;

public class LibraryUserAdminGetResponseBodyCreator {

    public static LibraryUserAdminGetResponseBody createLibraryUserAdminGetResponseBody() {
        Set<String> authorities = LibraryUserCreator.createValidUpdatedLibraryUser().getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        return LibraryUserAdminGetResponseBody.builder()
            .uuid(LibraryUserCreator.createValidLibraryUser().getUuid())
            .name(LibraryUserCreator.createValidLibraryUser().getName())
            .email(LibraryUserCreator.createValidLibraryUser().getEmail())
            .authorities(authorities)
            .build();
    }

}
