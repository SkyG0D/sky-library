package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPostRequestBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPutRequestBody;
import sky.skygod.skylibrary.exception.EmailExistsException;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.exception.RoleNotExistsException;
import sky.skygod.skylibrary.exception.UnauthorizedException;
import sky.skygod.skylibrary.mapper.LibraryUserMapper;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.model.Wrapper;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LibraryUserDetailsService implements UserDetailsService {

    private final LibraryUserRepository libraryUserRepository;
    private static final LibraryUserMapper MAPPER = LibraryUserMapper.INSTANCE;

    public Wrapper<?> list(Pageable pageable, Collection<? extends GrantedAuthority> authorities) {
        return new Wrapper<>(libraryUserRepository.findAll(pageable).map(toResponse(authorities)));
    }

    public Wrapper<?> findBy(String name, Pageable pageable, Collection<? extends GrantedAuthority> authorities) {
        return new Wrapper<>(libraryUserRepository
                .findByNameIgnoreCaseContaining(name, pageable).map(toResponse(authorities)));
    }

    public Wrapper<?> findByIdOrElseThrowNotFoundException(UUID uuid,
                                                           Collection<? extends GrantedAuthority> authorities) {
        LibraryUser user = libraryUserRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Library user not found"));

        return isAdmin(authorities)
                ? new Wrapper<>(MAPPER.toLibraryUserAdminGetResponseBody(user))
                : new Wrapper<>(MAPPER.toLibraryUserGetResponseBody(user));
    }

    public LibraryUserAdminGetResponseBody save(LibraryUserPostRequestBody libraryUserPostRequestBody,
                                                Collection<? extends GrantedAuthority> authorities) {
        LibraryUser user = MAPPER.toLibraryUser(libraryUserPostRequestBody);

        verifyIfRolesExistsOrElseThrowRoleNotExistsException(libraryUserPostRequestBody.getAuthorities());
        verifyIfUserHasPermissionOrElseThrowUnauthorizedException(user, authorities);
        verifyIfEmailNotExistsOrElseThrowEmailExistsException(user.getEmail());

        LibraryUser savedUser = libraryUserRepository.save(user);

        return MAPPER
                .toLibraryUserAdminGetResponseBody(savedUser);
    }

    public void delete(UUID uuid, Collection<? extends GrantedAuthority> authorities) {
        LibraryUser foundUser = MAPPER
                .toLibraryUser(findByIdToLibraryUserAdminGetResponseBody(uuid, authorities));

        verifyIfUserHasPermissionOrElseThrowUnauthorizedException(foundUser, authorities);

        libraryUserRepository.delete(foundUser);
    }

    public void replace(LibraryUserPutRequestBody libraryUserPutRequestBody,
                        Collection<? extends GrantedAuthority> authorities) {
        LibraryUser user = MAPPER.toLibraryUser(libraryUserPutRequestBody);

        verifyIfRolesExistsOrElseThrowRoleNotExistsException(libraryUserPutRequestBody.getAuthorities());
        verifyIfUserHasPermissionOrElseThrowUnauthorizedException(user, authorities);
        verifyIfUpdatedEmailNotExistsOrElseThrowEmailExistsException(libraryUserPutRequestBody, authorities);

        LibraryUserAdminGetResponseBody savedUser = findByIdToLibraryUserAdminGetResponseBody(
                libraryUserPutRequestBody.getUuid(), authorities);

        user.setUuid(savedUser.getUuid());
        libraryUserRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(libraryUserRepository.findByEmail(username))
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not found with email: \"" + username + "\".")
                );
    }

    private LibraryUserAdminGetResponseBody findByIdToLibraryUserAdminGetResponseBody(UUID uuid, Collection<? extends GrantedAuthority> authorities) {
        return (LibraryUserAdminGetResponseBody) findByIdOrElseThrowNotFoundException(uuid, authorities).get();
    }

    private Function<LibraryUser, ?> toResponse(Collection<? extends GrantedAuthority> authorities) {
        return isAdmin(authorities)
                ? MAPPER::toLibraryUserAdminGetResponseBody
                : MAPPER::toLibraryUserGetResponseBody;
    }

    private boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return MAPPER.mapAuthoritiesListToStringList(authorities).contains("ROLE_ADMIN");
    }

    private void verifyIfUserHasPermissionOrElseThrowUnauthorizedException(LibraryUser user,
                                                                           Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = MAPPER.mapAuthoritiesListToStringList(authorities);
        Set<String> userRoles = MAPPER.mapAuthoritiesListToStringList(user.getAuthorities());

        if (!roles.containsAll(userRoles)) {
            throw new UnauthorizedException(roles, "The user is not allowed to do this action");
        }

    }

    private void verifyIfEmailNotExistsOrElseThrowEmailExistsException(String email) {
        LibraryUser foundUser = libraryUserRepository.findByEmail(email);
        Optional.ofNullable(foundUser).ifPresent(user -> {
            throw new EmailExistsException(user.getEmail(), "Existing email: " + user.getEmail());
        });
    }

    private void verifyIfUpdatedEmailNotExistsOrElseThrowEmailExistsException(LibraryUserPutRequestBody libraryUser,
                                                                              Collection<? extends GrantedAuthority> authorities) {
        LibraryUserAdminGetResponseBody foundLibraryUser = findByIdToLibraryUserAdminGetResponseBody(
                libraryUser.getUuid(), authorities);
        if (!foundLibraryUser.getEmail().equals(libraryUser.getEmail())) {
            verifyIfEmailNotExistsOrElseThrowEmailExistsException(libraryUser.getEmail());
        }
    }

    private void verifyIfRolesExistsOrElseThrowRoleNotExistsException(Set<String> roles) {
        Set<String> existingRoles = Set.of("ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER");
        if (!existingRoles.containsAll(roles)) {
            throw new RoleNotExistsException(roles, "A role not exist: " + roles);
        }
    }

}
