package sky.skygod.skylibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPostRequestBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPutRequestBody;
import sky.skygod.skylibrary.model.LibraryUser;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class LibraryUserMapper {

    public static final LibraryUserMapper INSTANCE = Mappers.getMapper(LibraryUserMapper.class);

    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    public abstract LibraryUser toLibraryUser(LibraryUserPostRequestBody source);

    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    public abstract LibraryUser toLibraryUser(LibraryUserPutRequestBody source);

    public abstract LibraryUser toLibraryUser(LibraryUserAdminGetResponseBody source);

    public abstract LibraryUser toLibraryUser(LibraryUserGetResponseBody source);

    public abstract LibraryUserAdminGetResponseBody toLibraryUserAdminGetResponseBody(LibraryUser source);

    public abstract LibraryUserGetResponseBody toLibraryUserGetResponseBody(LibraryUser source);

    public Set<String> mapAuthoritiesListToStringList(Collection<? extends GrantedAuthority> source) {
        return source.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    String mapAuthoritiesListToString(Set<String> source) {
        return String.join(", ", source);
    }

    @Named("encodePassword")
    String encodePassword(String password) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
    }

}
