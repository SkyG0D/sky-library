package sky.skygod.skylibrary.integration;

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
import sky.skygod.skylibrary.dto.author.AuthorPutRequestBody;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.repository.author.AuthorRepository;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.author.AuthorCreator;
import sky.skygod.skylibrary.util.author.AuthorPostRequestBodyCreator;
import sky.skygod.skylibrary.util.author.AuthorPutRequestBodyCreator;
import sky.skygod.skylibrary.wrapper.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ActiveProfiles({"basic-security"})
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorControllerIT {

    private static final String API_URL = "/api/v1/authors";

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

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Autowired
    private AuthorRepository authorRepository;

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

    @Test
    @DisplayName("list returns list of authors inside page object when successful")
    void list_ReturnsListOfAuthorsInsidePageObject_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        PageableResponse<Author> authorPage = testRestTemplateRoleUser.exchange(API_URL, GET,
            null, new ParameterizedTypeReference<PageableResponse<Author>>() {
            }).getBody();

        assertThat(authorPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(savedAuthor);
    }

    @Test
    @DisplayName("list returns empty page when there are no authors")
    void list_ReturnsEmptyPage_WhenThereAreNoAuthors() {
        libraryUserRepository.save(USER);

        PageableResponse<Author> authorPage = testRestTemplateRoleUser.exchange(API_URL, GET,
            null, new ParameterizedTypeReference<PageableResponse<Author>>() {
            }).getBody();

        assertThat(authorPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns a author when successful")
    void findById_ReturnsAuthor_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        Author foundAuthor = testRestTemplateRoleUser.getForObject(API_URL + "/{uuid}", Author.class,
            savedAuthor.getUuid());

        assertThat(foundAuthor)
            .isNotNull()
            .isEqualTo(savedAuthor);
    }

    @Test
    @DisplayName("findBy returns a list of authors when successful")
    void findBy_ReturnsListOfAuthors_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        String url = String.format("%s/find-by?name=%s", API_URL, savedAuthor.getName());

        List<Author> authors = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<List<Author>>() {
            }).getBody();

        assertThat(authors)
            .isNotEmpty()
            .hasSize(1)
            .contains(savedAuthor);
    }

    @Test
    @DisplayName("findBy returns empty list when authors not found")
    void findBy_ReturnsEmptyList_WhenAuthorsNotFound() {
        libraryUserRepository.save(USER);

        String url = String.format("%s/find-by?name=%s", API_URL, "SkyG0D");

        List<Author> authors = testRestTemplateRoleUser.exchange(url, GET,
            null, new ParameterizedTypeReference<List<Author>>() {
            }).getBody();

        assertThat(authors)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns author when successful")
    void save_ReturnsAuthor_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        ResponseEntity<Author> authorResponseEntity = testRestTemplateRoleAdmin.postForEntity(API_URL + "/admin",
            AuthorPostRequestBodyCreator.createAuthorPostRequestBodyToBeSave(), Author.class);

        assertThat(authorResponseEntity).isNotNull();

        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(authorResponseEntity.getBody()).isNotNull();

        assertThat(authorResponseEntity.getBody().getUuid()).isNotNull();
    }

    @Test
    @DisplayName("save returns 403 forbidden when user is not admin")
    void save_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        ResponseEntity<Author> authorResponseEntity = testRestTemplateRoleUser.postForEntity(API_URL + "/admin",
            AuthorPostRequestBodyCreator.createAuthorPostRequestBodyToBeSave(), Author.class);

        assertThat(authorResponseEntity).isNotNull();

        assertThat(authorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete removes author when successful")
    void delete_RemovesAuthor_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/{uuid}", DELETE,
            null, Void.class, savedAuthor.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 forbidden when user is not admin")
    void delete_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/{uuid}",
            DELETE, null, Void.class, savedAuthor.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates author when successful")
    void replace_UpdatesAuthor_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        AuthorPutRequestBody authorPutRequestBodyToBeUpdate = AuthorPutRequestBodyCreator
            .createAuthorPutRequestBodyToBeUpdate();

        authorPutRequestBodyToBeUpdate.setUuid(savedAuthor.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(authorPutRequestBodyToBeUpdate), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns 403 forbidden when user is not admin")
    void replace_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Author savedAuthor = authorRepository.save(AuthorCreator.createAuthorToBeSaved());

        AuthorPutRequestBody authorPutRequestBodyToBeUpdate = AuthorPutRequestBodyCreator
            .createAuthorPutRequestBodyToBeUpdate();

        authorPutRequestBodyToBeUpdate.setUuid(savedAuthor.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(authorPutRequestBodyToBeUpdate), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}

