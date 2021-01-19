package sky.skygod.skylibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.user.LibraryUserAdminGetResponseBodyCreator;
import sky.skygod.skylibrary.util.user.LibraryUserCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPostRequestBodyCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPutRequestBodyCreator;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class LibraryUserDetailsServiceTest {

    @InjectMocks
    private LibraryUserDetailsService libraryUserDetailsService;

    @Mock
    private LibraryUserRepository libraryUserRepositoryMock;

    private Authentication userAuth;

    @BeforeEach
    void setup() {
        mockAuth();

        Page<LibraryUser> libraryUserPage = new PageImpl<>(
            List.of(LibraryUserCreator.createValidLibraryUser())
        );

        BDDMockito.when(libraryUserRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(libraryUserPage);

        BDDMockito.when(libraryUserRepositoryMock.findByNameIgnoreCaseContaining
            (ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(libraryUserPage);

        BDDMockito.when(libraryUserRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
            .thenReturn(LibraryUserCreator.createValidLibraryUser());

        BDDMockito
            .when(libraryUserRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.ofNullable(LibraryUserCreator.createValidLibraryUser()));

        BDDMockito.when(libraryUserRepositoryMock.save(ArgumentMatchers.any(LibraryUser.class)))
            .thenReturn(LibraryUserCreator.createValidLibraryUser());

        BDDMockito.doNothing()
            .when(libraryUserRepositoryMock)
            .delete(ArgumentMatchers.any(LibraryUser.class));
    }

    private void mockAuth() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);

        Set<String> roles = new HashSet<>(Set.of("ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"));
        Set authorities = roles.stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        Mockito.when(authenticationMock.getAuthorities())
            .thenReturn(authorities);

        userAuth = SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    @DisplayName("list returns list of library user inside page object when successful")
    void list_ReturnsListOfLibraryUserInsidePageObject_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        Page<LibraryUserAdminGetResponseBody> libraryUserPage = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserDetailsService.list(PageRequest.of(1, 1), userAuth).get();

        assertThat(libraryUserPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(validLibraryUser);

    }

    @Test
    @DisplayName("list returns empty page when there are no library users")
    void list_ReturnsEmptyPage_WhenThereAreNoLibraryUsers() {
        BDDMockito.when(libraryUserRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());

        Page<LibraryUserAdminGetResponseBody> libraryUsers = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserDetailsService.list(PageRequest.of(1, 1), userAuth).get();

        assertThat(libraryUsers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findBy returns list of library users inside page object when successful")
    void findBy_ReturnsListOfLibraryAuthorsInsidePageObject_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        Page<LibraryUserAdminGetResponseBody> libraryUsers = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserDetailsService.findBy("SkyG0D", PageRequest.of(1, 1), userAuth).get();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(validLibraryUser);
    }

    @Test
    @DisplayName("findBy returns empty page when library users is not found")
    void findBy_ReturnsEmptyPage_WhenLibraryUserIsNotFound() {
        BDDMockito.when(libraryUserRepositoryMock.findByNameIgnoreCaseContaining(
            ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)
        )).thenReturn(Page.empty());

        Page<LibraryUserAdminGetResponseBody> libraryUsers = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserDetailsService.findBy("SkyG0D", PageRequest.of(1, 1), userAuth).get();

        assertThat(libraryUsers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException returns library user when successful")
    void findByIdOrElseThrowNotFoundException_ReturnsLibraryUser_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        LibraryUserAdminGetResponseBody foundLibraryUser = (LibraryUserAdminGetResponseBody) libraryUserDetailsService
            .findByIdOrElseThrowNotFoundException(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"), userAuth)
            .get();

        assertThat(foundLibraryUser)
            .isNotNull()
            .isEqualTo(validLibraryUser);
    }

    @Test
    @DisplayName("save returns library user when successful")
    void save_ReturnsGender_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        LibraryUserAdminGetResponseBody savedLibraryUser = libraryUserDetailsService.save(
            LibraryUserPostRequestBodyCreator.createLibraryUserPostRequestBodyToBeSave(), userAuth);

        assertThat(savedLibraryUser)
            .isNotNull()
            .isEqualTo(validLibraryUser);
    }

    @Test
    @DisplayName("delete removes library user when successful")
    void delete_RemovesLibraryUser_WhenSuccessful() {
        assertThatCode(() -> libraryUserDetailsService.delete(UUID
            .fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"), userAuth)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace updates library user when successful")
    void replace_UpdatesLibraryUser_WhenSuccessful() {
        assertThatCode(() -> libraryUserDetailsService.replace(LibraryUserPutRequestBodyCreator
            .createLibraryUserPutRequestBodyToBeUpdate(), userAuth)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException throws NotFoundException when library user is not found")
    void findByIdOrElseThrowNotFoundException_ThrowsNotFoundException_WhenLibraryUserIsNotFound() {
        BDDMockito
            .when(libraryUserRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> libraryUserDetailsService.findByIdOrElseThrowNotFoundException(
            UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"), userAuth
        )).isInstanceOf(NotFoundException.class);
    }

}
