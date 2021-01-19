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
import sky.skygod.skylibrary.dto.book.BookGetResumedResponseBody;
import sky.skygod.skylibrary.dto.book.BookPostRequestBody;
import sky.skygod.skylibrary.dto.book.BookPutRequestBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.mapper.BookMapper;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.repository.book.BookFilter;
import sky.skygod.skylibrary.service.BookService;
import sky.skygod.skylibrary.util.book.BookCreator;
import sky.skygod.skylibrary.util.book.BookPostRequestBodyCreator;
import sky.skygod.skylibrary.util.book.BookPutRequestBodyCreator;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookServiceMock;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        PageImpl<Book> bookPage = new PageImpl<>(
            List.of(BookCreator.createValidBook()));

        BDDMockito.doNothing()
            .when(publisher).publishEvent(ArgumentMatchers.any());

        BDDMockito.when(bookServiceMock.resumed(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(bookPage.map(BookMapper.INSTANCE::toBookGetResumedResponseBody));

        BDDMockito.when(bookServiceMock.search(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(bookPage);

        BDDMockito
            .when(bookServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenReturn(BookCreator.createValidBook());

        BDDMockito.when(bookServiceMock.findByIsbn(ArgumentMatchers.anyLong()))
            .thenReturn(BookCreator.createValidBook());

        BDDMockito.when(bookServiceMock.save(ArgumentMatchers.any(BookPostRequestBody.class)))
            .thenReturn(BookCreator.createValidBook());

        BDDMockito.doNothing()
            .when(bookServiceMock)
            .delete(ArgumentMatchers.any(UUID.class));

        BDDMockito.doNothing()
            .when(bookServiceMock)
            .replace(ArgumentMatchers.any(BookPutRequestBody.class));
    }

    @Test
    @DisplayName("search returns list of book inside page object when successful")
    void search_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        Book book = BookCreator.createValidBook();

        BookFilter bookFilter = BookFilter
            .builder()
            .name(book.getName())
            .build();

        Page<Book> bookPage = bookController.search(bookFilter, null).getBody();

        assertThat(bookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(book);
    }

    @Test
    @DisplayName("search returns empty page when books is not found")
    void search_ReturnsEmptyPage_WhenBookIsNotFound() {
        BDDMockito.when(bookServiceMock.search(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(Page.empty());

        BookFilter bookFilter = BookFilter
            .builder()
            .name("SkyG0D")
            .build();

        Page<Book> bookPage = bookController.search(bookFilter, null).getBody();

        assertThat(bookPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("searchResumed returns list of book resumed inside page object when successful")
    void searchResumed_ReturnsListOfBookResumedInsidePageObject_WhenSuccessful() {
        Book book = BookCreator.createValidBook();

        BookGetResumedResponseBody resumedBook = BookMapper.INSTANCE.toBookGetResumedResponseBody(book);

        BookFilter bookFilter = BookFilter
            .builder()
            .name(book.getName())
            .build();

        Page<BookGetResumedResponseBody> resumedBookPage =
            bookController.searchResumed(bookFilter, null).getBody();

        assertThat(resumedBookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(resumedBook);
    }

    @Test
    @DisplayName("searchResumed returns empty page when books is not found")
    void searchResumed_ReturnsEmptyPage_WhenBooksIsNotFound() {
        BDDMockito.when(bookServiceMock.resumed(ArgumentMatchers.any(), ArgumentMatchers.isNull()))
            .thenReturn(Page.empty());

        BookFilter bookFilter = BookFilter
            .builder()
            .name("SkyG0D")
            .build();

        Page<BookGetResumedResponseBody> resumedBookPage =
            bookController.searchResumed(bookFilter, null).getBody();

        assertThat(resumedBookPage)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("findById returns a book when successful")
    void findById_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book foundBook = bookController.findById(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"))
            .getBody();

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("findByIsbn returns a book when successful")
    void findByIsbn_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book foundBook = bookController.findByIsbn(1L).getBody();

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("findByIsbn returns null when isbn is not found")
    void findByIsbn_ReturnsNull_WhenIsbnIsNotFound() {
        BDDMockito.when(bookServiceMock.findByIsbn(ArgumentMatchers.anyLong()))
            .thenReturn(null);

        Book foundBook = bookController.findByIsbn(1L).getBody();

        assertThat(foundBook).isNull();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        Book validBook = BookCreator.createValidBook();

        Book savedBook = bookController
            .save(BookPostRequestBodyCreator.createBookPostRequestBodyToBeSave(), null).getBody();

        assertThat(savedBook)
            .isNotNull()
            .isEqualTo(validBook);
    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        ResponseEntity<Void> entity = bookController
            .delete(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {
        ResponseEntity<Void> entity = bookController.replace(BookPutRequestBodyCreator
            .createBookPutRequestBodyToBeUpdate());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("findById throws NotFoundException when book does not exists")
    void findById_ThrowsNotFoundException_WhenBookDoesNotExists() {
        BDDMockito
            .when(bookServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenThrow(NotFoundException.class);

        assertThatThrownBy(
            () -> bookController.findById(UUID.fromString("d0261400-d579-4512-ad7e-be9a560b3ff9"))
        ).isInstanceOf(NotFoundException.class);
    }

}
