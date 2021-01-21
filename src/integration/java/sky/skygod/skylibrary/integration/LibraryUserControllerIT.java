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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import sky.skygod.skylibrary.controller.LibraryUserController;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPutRequestBody;
import sky.skygod.skylibrary.mapper.LibraryUserMapper;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.user.LibraryUserCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPostRequestBodyCreator;
import sky.skygod.skylibrary.util.user.LibraryUserPutRequestBodyCreator;
import sky.skygod.skylibrary.wrapper.PageableResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ActiveProfiles({"basic-security"})
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LibraryUserControllerIT {

    private static final String API_URL = "/api/v1/users";

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
    private LibraryUserController libraryUserController;

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
    @DisplayName("list returns list of library user inside page object for users when successful")
    void list_ReturnsListOfLibraryUserInsidePageObjectForUsers_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(USER);

        LibraryUserGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserGetResponseBody(savedLibraryUser);

        Page<LibraryUserGetResponseBody> libraryUsers = testRestTemplateRoleUser.exchange(API_URL,
            GET, null,
            new ParameterizedTypeReference<PageableResponse<LibraryUserGetResponseBody>>() {
            }).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("list returns list of library user inside page object for admins when successful")
    void list_ReturnsListOfLibraryUserInsidePageObjectForAdmins_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(USER);

        LibraryUserAdminGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserAdminGetResponseBody(savedLibraryUser);

        Page<LibraryUserAdminGetResponseBody> libraryUsers = testRestTemplateRoleUser.exchange(API_URL,
            GET, null,
            new ParameterizedTypeReference<PageableResponse<LibraryUserAdminGetResponseBody>>() {
            }).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("findBy returns list of library users inside page object for users when successful")
    void findBy_ReturnsListOfLibraryUsersInsidePageObjectForUsers_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(USER);

        LibraryUserGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserGetResponseBody(savedLibraryUser);

        String url = String.format("%s/find-by?name=%s", API_URL, libraryUserGetResponseBody.getName());

        Page<LibraryUserGetResponseBody> libraryUsers = testRestTemplateRoleUser.exchange(url,
            GET, null,
            new ParameterizedTypeReference<PageableResponse<LibraryUserGetResponseBody>>() {
            }).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("findBy returns list of library users inside page object for admins when successful")
    void findBy_ReturnsListOfLibraryUsersInsidePageObjectForAdmins_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(USER);

        LibraryUserAdminGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserAdminGetResponseBody(savedLibraryUser);

        String url = String.format("%s/find-by?name=%s", API_URL, libraryUserGetResponseBody.getName());

        Page<LibraryUserAdminGetResponseBody> libraryUsers = testRestTemplateRoleUser.exchange(url,
            GET, null,
            new ParameterizedTypeReference<PageableResponse<LibraryUserAdminGetResponseBody>>() {
            }).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("findBy returns empty page when library users is not found")
    void findBy_ReturnsEmptyPage_WhenLibraryUserIsNotFound() {
        libraryUserRepository.save(USER);

        String url = String.format("%s/find-by?name=%s", API_URL, "SkyG0D");

        Page<LibraryUserGetResponseBody> libraryUsers = testRestTemplateRoleUser.exchange(url,
            GET, null,
            new ParameterizedTypeReference<PageableResponse<LibraryUserGetResponseBody>>() {
            }).getBody();

        assertThat(libraryUsers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns library user for users when successful")
    void findById_ReturnsLibraryUserForUsers_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(USER);

        LibraryUserGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserGetResponseBody(savedLibraryUser);

        LibraryUserGetResponseBody foundLibraryUser = testRestTemplateRoleUser.getForObject(API_URL + "/{uuid}",
            LibraryUserGetResponseBody.class, libraryUserGetResponseBody.getUuid());

        assertThat(foundLibraryUser)
            .isNotNull()
            .isEqualTo(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("findById returns library user for admins when successful")
    void findById_ReturnsLibraryUserForAdmins_WhenSuccessful() {
        LibraryUser savedLibraryUser = libraryUserRepository.save(ADMIN);

        LibraryUserAdminGetResponseBody libraryUserGetResponseBody = LibraryUserMapper.INSTANCE
            .toLibraryUserAdminGetResponseBody(savedLibraryUser);

        LibraryUserAdminGetResponseBody foundLibraryUser = testRestTemplateRoleAdmin.getForObject(
            API_URL + "/{uuid}", LibraryUserAdminGetResponseBody.class, libraryUserGetResponseBody.getUuid());

        assertThat(foundLibraryUser)
            .isNotNull()
            .isEqualTo(libraryUserGetResponseBody);
    }

    @Test
    @DisplayName("save returns library user when successful")
    void save_ReturnsGender_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        ResponseEntity<LibraryUserAdminGetResponseBody> savedLibraryUser = testRestTemplateRoleAdmin.postForEntity(
            API_URL + "/admin", LibraryUserPostRequestBodyCreator.createLibraryUserPostRequestBodyToBeSave(),
            LibraryUserAdminGetResponseBody.class);

        assertThat(savedLibraryUser).isNotNull();

        assertThat(savedLibraryUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(savedLibraryUser.getBody()).isNotNull();

        assertThat(savedLibraryUser.getBody().getUuid()).isNotNull();
    }

    @Test
    @DisplayName("save returns 403 forbidden when user is not admin")
    void save_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        ResponseEntity<LibraryUserAdminGetResponseBody> savedLibraryUser = testRestTemplateRoleUser.postForEntity(
            API_URL + "/admin", LibraryUserPostRequestBodyCreator.createLibraryUserPostRequestBodyToBeSave(),
            LibraryUserAdminGetResponseBody.class);

        assertThat(savedLibraryUser).isNotNull();

        assertThat(savedLibraryUser.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete removes library user when successful")
    void delete_RemovesLibraryUser_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        LibraryUser savedLibraryUser = libraryUserRepository.save(LibraryUserCreator.createLibraryUserToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin/{uuid}",
            DELETE, null, Void.class, savedLibraryUser.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 forbidden when user is not admin")
    void delete_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        LibraryUser savedLibraryUser = libraryUserRepository.save(LibraryUserCreator.createLibraryUserToBeSaved());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin/{uuid}",
            DELETE, null, Void.class, savedLibraryUser.getUuid());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace updates library user when successful")
    void replace_UpdatesLibraryUser_WhenSuccessful() {
        libraryUserRepository.save(ADMIN);

        LibraryUser savedLibraryUser = libraryUserRepository.save(LibraryUserCreator.createLibraryUserToBeSaved());

        LibraryUserPutRequestBody libraryUserPutRequestBody =
            LibraryUserPutRequestBodyCreator.createLibraryUserPutRequestBodyToBeUpdate();

        libraryUserPutRequestBody.setUuid(savedLibraryUser.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleAdmin.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(libraryUserPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns 403 forbidden when user is not admin")
    void replace_Returns403Forbidden_WhenUserIsNotAdmin() {
        libraryUserRepository.save(USER);

        LibraryUser savedLibraryUser = libraryUserRepository.save(LibraryUserCreator.createLibraryUserToBeSaved());

        LibraryUserPutRequestBody libraryUserPutRequestBody =
            LibraryUserPutRequestBodyCreator.createLibraryUserPutRequestBodyToBeUpdate();

        libraryUserPutRequestBody.setUuid(savedLibraryUser.getUuid());

        ResponseEntity<Void> entity = testRestTemplateRoleUser.exchange(API_URL + "/admin", PUT,
            new HttpEntity<>(libraryUserPutRequestBody), Void.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
