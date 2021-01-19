package sky.skygod.skylibrary.util.user;

import org.springframework.security.core.GrantedAuthority;
import sky.skygod.skylibrary.dto.user.LibraryUserPostRequestBody;

import java.util.Set;
import java.util.stream.Collectors;

public class LibraryUserPostRequestBodyCreator {

    public static LibraryUserPostRequestBody createLibraryUserPostRequestBodyToBeSave() {
        Set<String> authorities = LibraryUserCreator.createValidLibraryUser().getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        return LibraryUserPostRequestBody
            .builder()
            .name(LibraryUserCreator.createValidLibraryUser().getName())
            .password(LibraryUserCreator.createValidLibraryUser().getPassword())
            .authorities(authorities)
            .build();
    }

}
