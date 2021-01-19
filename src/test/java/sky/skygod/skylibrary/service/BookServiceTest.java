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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.dto.book.BookGetResumedResponseBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.mapper.BookMapper;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.repository.book.BookFilter;
import sky.skygod.skylibrary.repository.book.BookRepository;
import sky.skygod.skylibrary.util.book.BookCreator;
import sky.skygod.skylibrary.util.book.BookPostRequestBodyCreator;
import sky.skygod.skylibrary.util.book.BookPutRequestBodyCreator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<Book> bookPage = new PageImpl<>(
            List.of(BookCreator.createValidBook()));

        BDDMockito.when(bookRepositoryMock.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(
            PageRequest.class)))
            .thenReturn(bookPage);

        BDDMockito
            .when(bookRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.ofNullable(BookCreator.createValidBook()));

        BDDMockito.when(bookRepositoryMock.findByIsbn(ArgumentMatchers.anyLong()))
            .thenReturn(BookCreator.createValidBook());

        BDDMockito.when(bookRepositoryMock.save(ArgumentMatchers.any(Book.class)))
            .thenReturn(BookCreator.createValidBook());

        BDDMockito.doNothing()
            .when(bookRepositoryMock)
            .delete(ArgumentMatchers.any(Book.class));
    }

    @Test
    @DisplayName("search returns list of book inside page object when successful")
    void search_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        Book book = BookCreator.createValidBook();

        BookFilter bookFilter = BookFilter
            .builder()
            .name(book.getName())
            .build();

        Page<Book> bookPage = bookService.search(bookFilter, PageRequest.of(1, 1));

        assertThat(bookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(book);
    }

    @Test
    @DisplayName("search returns empty page when books is not found")
    void search_ReturnsEmptyPage_WhenBookIsNotFound() {
        BDDMockito.when(bookRepositoryMock.findAll(ArgumentMatchers.any(Specification.class),
            ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());

        BookFilter bookFilter = BookFilter
            .builder()
            .name("SkyG0D")
            .build();

        Page<Book> bookPage = bookService.search(bookFilter, PageRequest.of(1, 1));

        assertThat(bookPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("resumed returns list of book resumed inside page object when successful")
    void resumed_ReturnsListOfBookResumedInsidePageObject_WhenSuccessful() {
        Book book = BookCreator.createValidBook();

        BookGetResumedResponseBody resumedBook = BookMapper.INSTANCE.toBookGetResumedResponseBody(book);

        BookFilter bookFilter = BookFilter
            .builder()
            .name(book.getName())
            .build();

        Page<BookGetResumedResponseBody> resumedBookPage = bookService
            .resumed(bookFilter, PageRequest.of(1, 1));

        assertThat(resumedBookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(resumedBook);
    }

    @Test
    @DisplayName("resumed returns empty page when books is not found")
    void resumed_ReturnsEmptyPage_WhenBooksIsNotFound() {
        BDDMockito.when(bookRepositoryMock.findAll(ArgumentMatchers.any(Specification.class),
            ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());

        BookFilter bookFilter = BookFilter
            .builder()
            .name("SkyG0D")
            .build();

        Page<BookGetResumedResponseBody> resumedBookPage =
            bookService.resumed(bookFilter, PageRequest.of(1, 1));

        assertThat(resumedBookPage)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException returns a book when successful")
    void findByIdOrElseThrowNotFoundException_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book foundBook = bookService.findByIdOrElseThrowNotFoundException(
            UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"));

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("findByIsbn returns a book when successful")
    void findByIsbn_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book foundBook = bookService.findByIsbn(1L);

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("findByIsbn returns null when isbn is not found")
    void findByIsbn_ReturnsNull_WhenIsbnIsNotFound() {
        BDDMockito.when(bookRepositoryMock.findByIsbn(ArgumentMatchers.anyLong()))
            .thenReturn(null);

        Book foundBook = bookService.findByIsbn(1L);

        assertThat(foundBook).isNull();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book savedBook = bookService.save(BookPostRequestBodyCreator.createBookPostRequestBodyToBeSave());

        assertThat(savedBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        assertThatCode(() -> bookService.delete(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9")))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {
        BDDMockito.when(bookRepositoryMock.save(ArgumentMatchers.any(Book.class)))
            .thenReturn(BookCreator.createValidBook());

        assertThatCode(() -> bookService.replace(BookPutRequestBodyCreator.createBookPutRequestBodyToBeUpdate()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException throws NotFoundException when book does not exists")
    void findByIdOrElseThrowNotFoundException_ThrowsNotFoundException_WhenBookDoesNotExists() {
        BDDMockito
            .when(bookRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            bookService.findByIdOrElseThrowNotFoundException(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"))
        ).isInstanceOf(NotFoundException.class);
    }

}
