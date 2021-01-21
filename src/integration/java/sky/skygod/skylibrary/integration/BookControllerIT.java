package sky.skygod.skylibrary.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import sky.skygod.skylibrary.dto.book.BookGetResumedResponseBody;
import sky.skygod.skylibrary.dto.book.BookPostRequestBody;
import sky.skygod.skylibrary.dto.book.BookPutRequestBody;
import sky.skygod.skylibrary.mapper.BookMapper;
import sky.skygod.skylibrary.model.*;
import sky.skygod.skylibrary.repository.author.AuthorRepository;
import sky.skygod.skylibrary.repository.book.BookRepository;
import sky.skygod.skylibrary.repository.gender.GenderRepository;
import sky.skygod.skylibrary.repository.publishingcompany.PublishingCompanyRepository;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.author.AuthorCreator;
import sky.skygod.skylibrary.util.book.BookCreator;
import sky.skygod.skylibrary.util.book.BookPostRequestBodyCreator;
import sky.skygod.skylibrary.util.book.BookPutRequestBodyCreator;
import sky.skygod.skylibrary.util.gender.GenderCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;
import sky.skygod.skylibrary.wrapper.PageableResponse;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ActiveProfiles({"basic-security"})
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerIT {

    private static final String API_URL = "/api/v1/books";

    private static final LibraryUser USER = LibraryUser.builder()
        .name("Nicole Gabriele")
        .email("nicole@hotmail.com")
        .password("{bcrypt}$2a$10$Z.68DB7zqF2XRtDyVzryouiQXl2QigN2DMxpePCEpYG89CTLP1WlW")
        .authorities("ROLE_USER")
        .build();

    private static final LibraryUser ADMIN = LibraryUser.builder()
        .name("SkyG0D")
        .email("skygod@hotmail.com")
        .password("{bcrypt}$2a$10$Z.68DB7zqF2XRtDyVzryouiQXl2QigN2DMxpePCEpYG89CTLP1WlW")
        .authorities("ROLE_USER, ROLE_ADMIN")
        .build();

    private static final Book BOOK = Book.builder()
        .name(BookCreator.createBookToBeSaved().getName())
        .status(BookCreator.createBookToBeSaved().getStatus())
        .description(BookCreator.createBookToBeSaved().getDescription())
        .pages(BookCreator.createBookToBeSaved().getPages())
        .isbn(BookCreator.createBookToBeSaved().getIsbn())
        .build();

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @TestConfiguration
    static class Config {

        @Bean(name = "testRestTemplateRoleUser")
        @Lazy
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthentication("nicole@hotmail.com", "SkyLibrary");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        @Lazy
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthentication("skygod@hotmail.com", "SkyLibrary");

            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private PublishingCompanyRepository publishingCompanyRepository;

    @BeforeEach
    void setup() {
        Author author = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        Gender gender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        PublishingCompany publishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        BOOK.setAuthors(Set.of(author));

        BOOK.setGenders(Set.of(gender));

        BOOK.setPublishingCompany(publishingCompany);
    }

    @Test
    @DisplayName("search returns list of book inside page object when successful")
    void search_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        String url = String.format("%s?name=%s", API_URL, savedBook.getName());

        PageableResponse<Book> bookPage = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<PageableResponse<Book>>() {
            }).getBody();

        assertThat(bookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(savedBook);
    }

    @Test
    @DisplayName("search returns empty page when books is not found")
    void search_ReturnsEmptyPage_WhenBookIsNotFound() {
        libraryUserRepository.save(USER);

        String url = String.format("%s?name=%s", API_URL, "SkyG0D");

        PageableResponse<Book> bookPage = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<PageableResponse<Book>>() {
            }).getBody();

        assertThat(bookPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("searchResumed returns list of book resumed inside page object when successful")
    void searchResumed_ReturnsListOfBookResumedInsidePageObject_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        String url = String.format("%s?resumed&name=%s", API_URL, savedBook.getName());

        BookGetResumedResponseBody resumedBook = BookMapper.INSTANCE.toBookGetResumedResponseBody(savedBook);

        PageableResponse<BookGetResumedResponseBody> resumedBookPage = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<PageableResponse<BookGetResumedResponseBody>>() {
            }).getBody();

        assertThat(resumedBookPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(resumedBook);
    }

    @Test
    @DisplayName("searchResumed returns empty page when books is not found")
    void searchResumed_ReturnsEmptyPage_WhenBooksIsNotFound() {
        libraryUserRepository.save(USER);

        String url = String.format("%s?resumed&name=%s", API_URL, "SkyG0D");

        PageableResponse<BookGetResumedResponseBody> resumedBookPage = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<PageableResponse<BookGetResumedResponseBody>>() {
            }).getBody();

        assertThat(resumedBookPage)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("findById returns a book when successful")
    void findById_ReturnsBook_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        Book foundBook = testRestTemplateRoleUser.getForObject(API_URL + "/{uuid}", Book.class,
            savedBook.getUuid());

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(savedBook);
    }

    @Test
    @DisplayName("findByIsbn returns a book when successful")
    void findByIsbn_ReturnsBook_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        Book foundBook = testRestTemplateRoleUser.getForObject(API_URL + "/by-isbn/{isbn}", Book.class,
            savedBook.getIsbn());

        assertThat(foundBook)
            .isNotNull()
            .isEqualTo(savedBook);
    }

    @Test
    @DisplayName("findByIsbn returns null when isbn is not found")
    void findByIsbn_ReturnsNull_WhenIsbnIsNotFound() {
        libraryUserRepository.save(USER);

        Book foundBook = testRestTemplateRoleUser.getForObject(API_URL + "/by-isbn/{isbn}", Book.class,
            1L);

        assertThat(foundBook).isNull();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        BookPostRequestBody bookPostRequestBodyToBeSave = BookPostRequestBodyCreator
            .createBookPostRequestBodyToBeSave();

        bookPostRequestBodyToBeSave.setAuthors(BOOK.getAuthors());
        bookPostRequestBodyToBeSave.setGenders(BOOK.getGenders());
        bookPostRequestBodyToBeSave.setPublishingCompany(BOOK.getPublishingCompany());

        ResponseEntity<Book> savedBook = testRestTemplateRoleAdmin.postForEntity(API_URL + "/admin",
            bookPostRequestBodyToBeSave, Book.class);

        assertThat(savedBook).isNotNull();

        assertThat(savedBook.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(savedBook.getBody()).isNotNull();

        assertThat(savedBook.getBody().getUuid()).isNotNull();
    }

    @Test
    @DisplayName("save returns 403 forbidden when user is not found")
    void save_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        ResponseEntity<Void> savedBook = testRestTemplateRoleUser.postForEntity(API_URL + "/admin",
            new Book(), Void.class);

        assertThat(savedBook).isNotNull();

        assertThat(savedBook.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Book savedBook = bookRepository.save(BOOK);

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/{uuid}", DELETE,
            null, Void.class, savedBook.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 forbidden when user is not admin")
    void delete_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/{uuid}", DELETE,
            null, Void.class, savedBook.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Book savedBook = bookRepository.save(BOOK);

        BookPutRequestBody bookPutRequestBodyToBeUpdate = BookPutRequestBodyCreator
            .createBookPutRequestBodyToBeUpdate();

        bookPutRequestBodyToBeUpdate.setUuid(savedBook.getUuid());

        bookPutRequestBodyToBeUpdate.setAuthors(savedBook.getAuthors());

        bookPutRequestBodyToBeUpdate.setGenders(savedBook.getGenders());

        bookPutRequestBodyToBeUpdate.setPublishingCompany(savedBook.getPublishingCompany());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/", PUT,
            new HttpEntity<>(bookPutRequestBodyToBeUpdate), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns 403 forbidden when user is not admin")
    void replace_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Book savedBook = bookRepository.save(BOOK);

        BookPutRequestBody bookPutRequestBodyToBeUpdate = BookPutRequestBodyCreator
            .createBookPutRequestBodyToBeUpdate();

        bookPutRequestBodyToBeUpdate.setUuid(savedBook.getUuid());

        bookPutRequestBodyToBeUpdate.setAuthors(savedBook.getAuthors());

        bookPutRequestBodyToBeUpdate.setGenders(savedBook.getGenders());

        bookPutRequestBodyToBeUpdate.setPublishingCompany(savedBook.getPublishingCompany());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/", PUT,
            new HttpEntity<>(bookPutRequestBodyToBeUpdate), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

}
