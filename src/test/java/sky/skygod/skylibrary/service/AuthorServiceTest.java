package sky.skygod.skylibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.repository.author.AuthorRepository;
import sky.skygod.skylibrary.util.author.AuthorCreator;
import sky.skygod.skylibrary.util.author.AuthorPostRequestBodyCreator;
import sky.skygod.skylibrary.util.author.AuthorPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<Author> authorPage = new PageImpl<>(
            List.of(AuthorCreator.createValidAuthor()));

        BDDMockito.when(authorRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(authorPage);

        BDDMockito
            .when(authorRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.ofNullable(AuthorCreator.createValidAuthor()));

        BDDMockito.when(authorRepositoryMock.findByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
            .thenReturn(List.of(AuthorCreator.createValidAuthor()));

        BDDMockito.when(authorRepositoryMock.save(ArgumentMatchers.any(Author.class)))
            .thenReturn(AuthorCreator.createValidAuthor());

        BDDMockito.doNothing()
            .when(authorRepositoryMock)
            .delete(ArgumentMatchers.any(Author.class));
    }

    @Test
    @DisplayName("list returns list of authors inside page object when successful")
    void list_ReturnsListOfAuthorsInsidePageObject_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Page<Author> authorPage = authorService.list(PageRequest.of(1, 1));

        assertThat(authorPage).isNotNull();

        assertThat(authorPage.toList())
            .isNotEmpty()
            .hasSize(1)
            .contains(validAuthor);
    }

    @Test
    @DisplayName("list returns empty page when there are no authors")
    void list_ReturnsEmptyPage_WhenThereAreNoAuthors() {
        BDDMockito.when(authorRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());

        Page<Author> authorPage = authorService.list(PageRequest.of(1, 1));

        assertThat(authorPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns a author when successful")
    void findByIdOrElseThrowNotFoundException_ReturnsAuthor_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Author foundAuthor = authorService.findByIdOrElseThrowNotFoundException(
            UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"));

        assertThat(foundAuthor)
            .isNotNull()
            .isEqualTo(validAuthor);
        ;
    }

    @Test
    @DisplayName("findBy returns a list of authors when successful")
    void findBy_ReturnsListOfAuthors_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        List<Author> authors = authorService.findBy("SkyG0D");

        assertThat(authors)
            .isNotEmpty()
            .hasSize(1)
            .contains(validAuthor);
    }

    @Test
    @DisplayName("findBy returns empty list when authors not found")
    void findBy_ReturnsEmptyList_WhenAuthorsNotFound() {
        BDDMockito.when(authorRepositoryMock.findByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
            .thenReturn(Collections.emptyList());

        List<Author> authors = authorService.findBy("SkyG0D");

        assertThat(authors)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns author when successful")
    void save_ReturnsAuthor_WhenSuccessful() {
        Author validAuthor = AuthorCreator.createValidAuthor();

        Author savedAuthor = authorService.save(AuthorPostRequestBodyCreator.createAuthorPostRequestBodyToBeSave());

        assertThat(savedAuthor)
            .isNotNull()
            .isEqualTo(validAuthor);
    }

    @Test
    @DisplayName("delete removes author when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        assertThatCode(() -> authorService.delete(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871")))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace updates author when successful")
    void replace_UpdatesAuthor_WhenSuccessful() {
        BDDMockito.when(authorRepositoryMock.save(ArgumentMatchers.any(Author.class)))
            .thenReturn(AuthorCreator.createValidUpdatedAuthor());

        assertThatCode(() -> authorService.replace(AuthorPutRequestBodyCreator.createAuthorPutRequestBodyToBeUpdate()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findById throws NotFoundException when author does not exists")
    void findByIdOrElseThrowNotFoundException_ThrowsNotFoundException_WhenAuthorDoesNotExists() {
        BDDMockito
            .when(authorRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            authorService.findByIdOrElseThrowNotFoundException(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
        ).isInstanceOf(NotFoundException.class);
    }
}
