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
import sky.skygod.skylibrary.dto.author.AuthorPostRequestBody;
import sky.skygod.skylibrary.dto.author.AuthorPutRequestBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.service.AuthorService;
import sky.skygod.skylibrary.util.author.AuthorCreator;
import sky.skygod.skylibrary.util.author.AuthorPostRequestBodyCreator;
import sky.skygod.skylibrary.util.author.AuthorPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class AuthorControllerTest {

    @InjectMocks
    private AuthorController authorController;

    @Mock
    private AuthorService authorServiceMock;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        PageImpl<Author> authorPage = new PageImpl<>(
            List.of(AuthorCreator.createValidAuthor()));

        BDDMockito.doNothing()
            .when(publisher).publishEvent(ArgumentMatchers.any());

        BDDMockito.when(authorServiceMock.list(ArgumentMatchers.any()))
            .thenReturn(authorPage);

        BDDMockito
            .when(authorServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenReturn(AuthorCreator.createValidAuthor());

        BDDMockito.when(authorServiceMock.findBy(ArgumentMatchers.anyString()))
            .thenReturn(List.of(AuthorCreator.createValidAuthor()));

        BDDMockito.when(authorServiceMock.save(ArgumentMatchers.any(AuthorPostRequestBody.class)))
            .thenReturn(AuthorCreator.createValidAuthor());

        BDDMockito.doNothing()
            .when(authorServiceMock)
            .delete(ArgumentMatchers.any(UUID.class));

        BDDMockito.doNothing()
            .when(authorServiceMock)
            .replace(ArgumentMatchers.any(AuthorPutRequestBody.class));
    }

    @Test
    @DisplayName("list returns list of authors inside page object when successful")
    void list_ReturnsListOfAuthorsInsidePageObject_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Page<Author> authorPage = authorController.list(null).getBody();

        assertThat(authorPage).isNotNull();

        assertThat(authorPage.toList())
            .isNotEmpty()
            .hasSize(1)
            .contains(validAuthor);
    }

    @Test
    @DisplayName("list returns empty page when there are no authors")
    void list_ReturnsEmptyPage_WhenThereAreNoAuthors() {
        BDDMockito.when(authorServiceMock.list(ArgumentMatchers.any()))
            .thenReturn(Page.empty());

        Page<Author> authorPage = authorController.list(null).getBody();

        assertThat(authorPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns a author when successful")
    void findById_ReturnsAuthor_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Author foundAuthor = authorController.findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
            .getBody();

        assertThat(foundAuthor)
            .isNotNull()
            .isEqualTo(validAuthor);
    }

    @Test
    @DisplayName("findBy returns a list of authors when successful")
    void findBy_ReturnsListOfAuthors_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        List<Author> authors = authorController.findBy("SkyG0D").getBody();

        assertThat(authors)
            .isNotEmpty()
            .hasSize(1)
            .contains(validAuthor);
    }

    @Test
    @DisplayName("findBy returns empty list when authors not found")
    void findBy_ReturnsEmptyList_WhenAuthorsNotFound() {
        BDDMockito.when(authorServiceMock.findBy(ArgumentMatchers.anyString()))
            .thenReturn(Collections.emptyList());

        List<Author> authors = authorController.findBy("SkyG0D").getBody();

        assertThat(authors)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns author when successful")
    void save_ReturnsAuthor_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Author savedAuthor = authorController
            .save(AuthorPostRequestBodyCreator.createAuthorPostRequestBodyToBeSave(), null).getBody();

        assertThat(savedAuthor)
            .isNotNull()
            .isEqualTo(validAuthor);
    }

    @Test
    @DisplayName("delete removes author when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        ResponseEntity<Void> entity = authorController
            .delete(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates author when successful")
    void replace_UpdatesAuthor_WhenSuccessful() {
        ResponseEntity<Void> entity = authorController.replace(AuthorPutRequestBodyCreator
            .createAuthorPutRequestBodyToBeUpdate());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("findById throws NotFoundException when author does not exists")
    void findById_ThrowsNotFoundException_WhenAuthorDoesNotExists() {
        BDDMockito
            .when(authorServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenThrow(NotFoundException.class);

        assertThatThrownBy(
            () -> authorController.findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
        ).isInstanceOf(NotFoundException.class);
    }

}
