package sky.skygod.skylibrary.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPostRequestBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPutRequestBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.Wrapper;
import sky.skygod.skylibrary.service.LibraryUserDetailsService;
import sky.skygod.skylibrary.util.user.LibraryUserAdminGetResponseBodyCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPostRequestBodyCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPutRequestBodyCreator;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class LibraryUserControllerTest {

    @InjectMocks
    private LibraryUserController libraryUserController;

    @Mock
    private LibraryUserDetailsService libraryUserDetailsServiceMock;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        Wrapper<Object> pageWrapper = new Wrapper<>(
            new PageImpl<>(List.of(LibraryUserAdminGetResponseBodyCreator.createLibraryUserAdminGetResponseBody()))
        );

        BDDMockito.doNothing()
            .when(publisher).publishEvent(ArgumentMatchers.any());

        BDDMockito.when(libraryUserDetailsServiceMock.list(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(pageWrapper);

        BDDMockito.when(libraryUserDetailsServiceMock.findBy(ArgumentMatchers.any(), ArgumentMatchers.isNull(),
            ArgumentMatchers.isNull()))
            .thenReturn(pageWrapper);

        BDDMockito
            .when(libraryUserDetailsServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class),
                ArgumentMatchers.isNull()))
            .thenReturn(new Wrapper<>(LibraryUserAdminGetResponseBodyCreator.createLibraryUserAdminGetResponseBody()));

        BDDMockito.when(libraryUserDetailsServiceMock.save(ArgumentMatchers.any(LibraryUserPostRequestBody.class),
            ArgumentMatchers.isNull()))
            .thenReturn(LibraryUserAdminGetResponseBodyCreator.createLibraryUserAdminGetResponseBody());

        BDDMockito.doNothing()
            .when(libraryUserDetailsServiceMock)
            .delete(ArgumentMatchers.any(UUID.class), ArgumentMatchers.isNull());

        BDDMockito.doNothing()
            .when(libraryUserDetailsServiceMock)
            .replace(ArgumentMatchers.any(LibraryUserPutRequestBody.class), ArgumentMatchers.isNull());

    }

    @Test
    @DisplayName("list returns list of library author inside page object when successful")
    void list_ReturnsListOfLibraryAuthorInsidePageObject_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        Page<LibraryUserAdminGetResponseBody> foundLibraryUser =
            (Page<LibraryUserAdminGetResponseBody>) libraryUserController.list(null).getBody();

        assertThat(foundLibraryUser)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(validLibraryUser);

    }

    @Test
    @DisplayName("list returns empty page when there are no library users")
    void list_ReturnsEmptyPage_WhenThereAreNoLibraryUsers() {
        BDDMockito.when(libraryUserDetailsServiceMock.list(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(new Wrapper<>(Page.empty()));

        Page<LibraryUserAdminGetResponseBody> libraryUsers = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserController.list(null).getBody();

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
            libraryUserController.findBy("SkyG0D", null).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(validLibraryUser);
    }

    @Test
    @DisplayName("findBy returns empty page when library users is not found")
    void findBy_ReturnsEmptyPage_WhenLibraryUserIsNotFound() {
        BDDMockito.when(libraryUserDetailsServiceMock.findBy(ArgumentMatchers.any(), ArgumentMatchers.isNull(),
            ArgumentMatchers.isNull())).thenReturn(new Wrapper<>(Page.empty()));

        Page<LibraryUserAdminGetResponseBody> libraryUsers = (Page<LibraryUserAdminGetResponseBody>)
            libraryUserController.findBy("SkyG0D", null).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException returns library user when successful")
    void findByIdOrElseThrowNotFoundException_ReturnsLibraryUser_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        LibraryUserAdminGetResponseBody foundLibraryUser = (LibraryUserAdminGetResponseBody)
            libraryUserController.findById(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85")).getBody();

        assertThat(foundLibraryUser)
            .isNotNull()
            .isEqualTo(validLibraryUser);
    }

    @Test
    @DisplayName("save returns library user when successful")
    void save_ReturnsGender_WhenSuccessful() {
        LibraryUserAdminGetResponseBody validLibraryUser = LibraryUserAdminGetResponseBodyCreator
            .createLibraryUserAdminGetResponseBody();

        LibraryUserAdminGetResponseBody savedLibraryUser = libraryUserController.save(
            LibraryUserPostRequestBodyCreator.createLibraryUserPostRequestBodyToBeSave(), null).getBody();

        assertThat(savedLibraryUser)
            .isNotNull()
            .isEqualTo(validLibraryUser);
    }

    @Test
    @DisplayName("delete removes library user when successful")
    void delete_RemovesLibraryUser_WhenSuccessful() {
        ResponseEntity<Void> entity = libraryUserController
            .delete(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85"));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates library user when successful")
    void replace_UpdatesLibraryUser_WhenSuccessful() {
        ResponseEntity<Void> entity = libraryUserController.replace(
            LibraryUserPutRequestBodyCreator.createLibraryUserPutRequestBodyToBeUpdate()
        );

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException throws NotFoundException when library user is not found")
    void findByIdOrElseThrowNotFoundException_ThrowsNotFoundException_WhenLibraryUserIsNotFound() {
        BDDMockito
            .when(libraryUserDetailsServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class),
                ArgumentMatchers.isNull()))
            .thenThrow(NotFoundException.class);

        assertThatThrownBy(() ->
            libraryUserController.findById(UUID.fromString("85155dd2-87dd-425f-aadd-196ac1c0df85")))
            .isInstanceOf(NotFoundException.class);
    }

}
