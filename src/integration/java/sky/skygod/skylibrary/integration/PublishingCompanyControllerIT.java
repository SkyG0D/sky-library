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
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPutRequestBody;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.repository.publishingcompany.PublishingCompanyRepository;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPostRequestBodyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPutRequestBodyCreator;
import sky.skygod.skylibrary.wrapper.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ActiveProfiles({"basic-security"})
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PublishingCompanyControllerIT {

    private static final String API_URL = "/api/v1/publishers";

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
    private PublishingCompanyRepository publishingCompanyRepository;

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
    @DisplayName("list returns list of publishers inside page object when successful")
    void list_ReturnsListOfPublishersInsidePageObject_WhenSuccessful() {
        libraryUserRepository.save(USER);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        PageableResponse<PublishingCompany> publishingCompanyPage = testRestTemplateRoleUser.exchange(API_URL, GET,
            null, new ParameterizedTypeReference<PageableResponse<PublishingCompany>>() {
            }).getBody();

        assertThat(publishingCompanyPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(savedPublishingCompany);
    }

    @Test
    @DisplayName("list returns empty page when there are no publishers")
    void list_ReturnsEmptyPage_WhenThereAreNoPublishers() {
        libraryUserRepository.save(USER);

        PageableResponse<PublishingCompany> publishingCompanyPage = testRestTemplateRoleUser.exchange(API_URL, GET,
            null, new ParameterizedTypeReference<PageableResponse<PublishingCompany>>() {
            }).getBody();

        assertThat(publishingCompanyPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns a publishing company when successful")
    void findById_ReturnsPublishingCompany_WhenSuccessful() {
        libraryUserRepository.save(USER);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        PublishingCompany foundPublishingCompany = testRestTemplateRoleUser.getForObject(API_URL + "/{uuid}",
            PublishingCompany.class, savedPublishingCompany.getUuid());

        assertThat(foundPublishingCompany)
            .isNotNull()
            .isEqualTo(savedPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns a list of publishers when successful")
    void findBy_ReturnsListOfPublishers_WhenSuccessful() {
        libraryUserRepository.save(USER);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        String url = String.format("%s/find-by?name=%s", API_URL, savedPublishingCompany.getName());

        List<PublishingCompany> publishers = testRestTemplateRoleUser.exchange(url, GET, null,
            new ParameterizedTypeReference<List<PublishingCompany>>() {
            }).getBody();

        assertThat(publishers)
            .isNotEmpty()
            .hasSize(1)
            .contains(savedPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns empty list when publishers not found")
    void findBy_ReturnsEmptyList_WhenPublishersNotFound() {
        libraryUserRepository.save(USER);

        String url = String.format("%s/find-by?name=%s", API_URL, "SkyG0D");

        List<PublishingCompany> publishers = testRestTemplateRoleUser.exchange(url, GET, null,
            new ParameterizedTypeReference<List<PublishingCompany>>() {
            }).getBody();

        assertThat(publishers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns publishing company when successful")
    void save_ReturnsPublishingCompany_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        ResponseEntity<PublishingCompany> savedPublishingCompany = testRestTemplateRoleAdmin.postForEntity(API_URL + "/admin",
            PublishingCompanyPostRequestBodyCreator.createPublishingCompanyPostRequestBodyToBeSaved(),
            PublishingCompany.class);

        assertThat(savedPublishingCompany).isNotNull();

        assertThat(savedPublishingCompany.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(savedPublishingCompany.getBody()).isNotNull();

        assertThat(savedPublishingCompany.getBody().getUuid()).isNotNull();
    }

    @Test
    @DisplayName("save returns 403 forbidden when is not admin")
    void save_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        ResponseEntity<PublishingCompany> savedPublishingCompany = testRestTemplateRoleUser
            .postForEntity(API_URL + "/admin",
            PublishingCompanyPostRequestBodyCreator.createPublishingCompanyPostRequestBodyToBeSaved(),
            PublishingCompany.class);

        assertThat(savedPublishingCompany).isNotNull();

        assertThat(savedPublishingCompany.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete removes publishing company when successful")
    void delete_RemovesPublishingCompany_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/{uuid}", DELETE,
            null, Void.class, savedPublishingCompany.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 forbidden when is not admin")
    void delete_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/{uuid}", DELETE,
            null, Void.class, savedPublishingCompany.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates publishing company when successful")
    void replace_UpdatesPublishingCompany_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        PublishingCompanyPutRequestBody publishingCompanyPutRequestBody = PublishingCompanyPutRequestBodyCreator
            .createPublishingCompanyPutRequestBodyToBeUpdate();

        publishingCompanyPutRequestBody.setUuid(savedPublishingCompany.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(publishingCompanyPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns 403 forbidden when is not admin")
    void replace_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(PublishingCompanyCreator
            .createPublishingCompanyToBeSaved());

        PublishingCompanyPutRequestBody publishingCompanyPutRequestBody = PublishingCompanyPutRequestBodyCreator
            .createPublishingCompanyPutRequestBodyToBeUpdate();

        publishingCompanyPutRequestBody.setUuid(savedPublishingCompany.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(publishingCompanyPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
