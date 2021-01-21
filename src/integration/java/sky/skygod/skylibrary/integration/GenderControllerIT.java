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
import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.repository.gender.GenderRepository;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.gender.GenderCreator;
import sky.skygod.skylibrary.util.gender.GenderPostRequestBodyCreator;
import sky.skygod.skylibrary.util.gender.GenderPutRequestBodyCreator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ActiveProfiles({"basic-security"})
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenderControllerIT {

    private static final String API_URL = "/api/v1/genders";

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
    private GenderRepository genderRepository;

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
    @DisplayName("list returns list of gender when successful")
    void list_ReturnsListOfGender_WhenSuccessful() {
        libraryUserRepository.save(USER);

        Gender savedGender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        List<Gender> genders = testRestTemplateRoleUser.exchange(API_URL, GET, null,
            new ParameterizedTypeReference<List<Gender>>() {
            }).getBody();

        assertThat(genders)
            .isNotEmpty()
            .hasSize(1)
            .contains(savedGender);
    }

    @Test
    @DisplayName("list returns empty list when there are no genders")
    void list_ReturnsEmptyList_WhenThereAreNoGenders() {
        libraryUserRepository.save(USER);

        List<Gender> genders = testRestTemplateRoleUser.exchange(API_URL, GET, null,
            new ParameterizedTypeReference<List<Gender>>() {
            }).getBody();

        assertThat(genders)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("save returns gender when successful")
    void save_ReturnsGender_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        ResponseEntity<Gender> savedGender = testRestTemplateRoleAdmin.postForEntity(API_URL + "/admin",
            GenderPostRequestBodyCreator.createGenderPostRequestBodyToBeSave(), Gender.class);

        assertThat(savedGender).isNotNull();

        assertThat(savedGender.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(savedGender.getBody()).isNotNull();

        assertThat(savedGender.getBody().getUuid()).isNotNull();
    }

    @Test
    @DisplayName("save returns 403 forbidden when user is not admin")
    void save_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        ResponseEntity<Gender> savedGender = testRestTemplateRoleUser.postForEntity(API_URL + "/admin",
            GenderPostRequestBodyCreator.createGenderPostRequestBodyToBeSave(), Gender.class);

        assertThat(savedGender).isNotNull();

        assertThat(savedGender.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete removes gender when successful")
    void delete_RemovesGender_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Gender savedGender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/{uuid}",
            DELETE, null, Void.class, savedGender.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 forbidden when user is not admin")
    void delete_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Gender savedGender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/{uuid}",
            DELETE, null, Void.class, savedGender.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates gender when successful")
    void replace_UpdatesGender_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        Gender savedGender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        GenderPutRequestBody genderPutRequestBody = GenderPutRequestBodyCreator
            .createGenderPutRequestBodyToBeUpdate();

        genderPutRequestBody.setUuid(savedGender.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(genderPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns 403 forbidden when user is not admin")
    void replace_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        Gender savedGender = genderRepository.save(GenderCreator.createGenderToBeSaved());

        GenderPutRequestBody genderPutRequestBody = GenderPutRequestBodyCreator
            .createGenderPutRequestBodyToBeUpdate();

        genderPutRequestBody.setUuid(savedGender.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(genderPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
