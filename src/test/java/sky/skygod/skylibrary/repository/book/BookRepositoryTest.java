package sky.skygod.skylibrary.repository.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.util.book.BookCreator;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests for Book Repository")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("save persists book when successful")
    void save_PersistsBook_WhenSuccessful() {
        Book book = BookCreator.createBookToBeSaved();

        Book savedBook = bookRepository.save(book);

        System.out.println(book);
        System.out.println(savedBook);

        assertThat(savedBook).isNotNull();

        assertThat(savedBook.getUuid()).isNotNull();

        assertThat(savedBook.getName())
            .isNotNull()
            .isEqualTo(book.getName());

        assertThat(savedBook.getIsbn())
            .isNotNull()
            .isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("save updates book when successful")
    void save_UpdatesBook_WhenSuccessful() {
        Book book = BookCreator.createBookToBeSaved();

        Book savedBook = bookRepository.save(book);

        savedBook.setName("Estrutura de dados com JS");

        Book updatedBook = bookRepository.save(savedBook);

        assertThat(updatedBook).isNotNull();

        assertThat(updatedBook.getUuid())
            .isNotNull()
            .isEqualTo(savedBook.getUuid());

        assertThat(updatedBook.getName())
            .isNotNull()
            .isEqualTo(savedBook.getName());

        assertThat(updatedBook.getIsbn())
            .isNotNull()
            .isEqualTo(savedBook.getIsbn());
    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesBook_WhenSuccessful() {
        Book book = BookCreator.createBookToBeSaved();

        Book savedBook = bookRepository.save(book);

        bookRepository.delete(savedBook);

        Optional<Book> optionalBook = bookRepository.findById(savedBook.getUuid());

        assertThat(optionalBook).isEmpty();
    }

    @Test
    @DisplayName("findById returns book when successful")
    void findById_ReturnsBook_WhenSuccessful() {
        Book book = BookCreator.createBookToBeSaved();

        Book savedBook = bookRepository.save(book);

        Optional<Book> optionalBook = bookRepository.findById(savedBook.getUuid());

        assertThat(optionalBook).isNotEmpty();

        assertThat(optionalBook.get()).isEqualTo(savedBook);
    }

    @Test
    @DisplayName("findById returns empty optional when uuid is invalid")
    void findById_ReturnsEmptyOptional_WhenUuidIsInvalid() {
        Optional<Book> optionalBook = bookRepository
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4872"));

        assertThat(optionalBook).isEmpty();
    }

    @Test
    @DisplayName("save throws ConstraintViolationException when an required column is null")
    void save_ThrowsConstraintViolationException_WhenAnRequiredColumnIsNull() {
        Book book = new Book();

        assertThatThrownBy(() -> bookRepository.saveAndFlush(book))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
