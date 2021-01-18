package sky.skygod.skylibrary.repository.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.util.author.AuthorCreator;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests for Author Repository")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("save persists author when successful")
    void save_PersistsAuthor_WhenSuccessful() {
        Author author = AuthorCreator.createAuthorToBeSaved();

        Author savedAuthor = authorRepository.save(author);

        assertThat(savedAuthor).isNotNull();

        assertThat(savedAuthor.getUuid()).isNotNull();

        assertThat(savedAuthor.getName())
            .isNotNull()
            .isEqualTo(author.getName());
    }

    @Test
    @DisplayName("save updates author when successful")
    void save_UpdatesAuthor_WhenSuccessful() {
        Author author = AuthorCreator.createAuthorToBeSaved();

        Author savedAuthor = authorRepository.save(author);

        savedAuthor.setName("Silvano Marques");

        Author updatedAuthor = authorRepository.save(savedAuthor);

        assertThat(updatedAuthor).isNotNull();

        assertThat(updatedAuthor.getUuid())
            .isNotNull()
            .isEqualTo(savedAuthor.getUuid());

        assertThat(updatedAuthor.getName())
            .isNotNull()
            .isEqualTo(savedAuthor.getName());
    }

    @Test
    @DisplayName("delete removes author when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        Author author = AuthorCreator.createAuthorToBeSaved();

        Author savedAuthor = authorRepository.save(author);

        authorRepository.delete(savedAuthor);

        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getUuid());

        assertThat(optionalAuthor).isEmpty();
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase returns list of author when successful")
    void findByNameContainingIgnoreCase_ReturnsListOfAuthor_WhenSuccessful() {
        Author author = AuthorCreator.createAuthorToBeSaved();

        Author savedAuthor = authorRepository.save(author);

        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(savedAuthor.getName());

        assertThat(authors)
            .isNotNull()
            .hasSize(1)
            .contains(savedAuthor);
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase returns empty list when no author is found")
    void findByNameContainingIgnoreCase_ReturnsEmptyList_WhenAuthorIsNotFound() {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase("SkyG0D");

        assertThat(authors).isEmpty();
    }

    @Test
    @DisplayName("findById returns author when successful")
    void findById_ReturnsAuthor_WhenSuccessful() {
        Author author = AuthorCreator.createAuthorToBeSaved();

        Author savedAuthor = authorRepository.save(author);

        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getUuid());

        assertThat(optionalAuthor).isNotEmpty();

        assertThat(optionalAuthor.get()).isEqualTo(savedAuthor);
    }

    @Test
    @DisplayName("findById returns empty optional when uuid is invalid")
    void findById_ReturnsEmptyOptional_WhenUuidIsInvalid() {
        Optional<Author> optionalAuthor = authorRepository
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4872"));

        assertThat(optionalAuthor).isEmpty();
    }

    @Test
    @DisplayName("save throws ConstraintViolationException when an required column is null")
    void save_ThrowsConstraintViolationException_WhenAnRequiredColumnIsNull() {
        Author author = new Author();

        assertThatThrownBy(() -> authorRepository.saveAndFlush(author))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
