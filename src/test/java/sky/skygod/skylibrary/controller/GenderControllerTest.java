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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.dto.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.service.GenderService;
import sky.skygod.skylibrary.util.gender.GenderCreator;
import sky.skygod.skylibrary.util.gender.GenderPostRequestBodyCreator;
import sky.skygod.skylibrary.util.gender.GenderPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class GenderControllerTest {

    @InjectMocks
    private GenderController genderController;

    @Mock
    private GenderService genderServiceMock;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        BDDMockito.doNothing()
            .when(publisher).publishEvent(ArgumentMatchers.any());

        BDDMockito.when(genderServiceMock.list())
            .thenReturn(List.of(GenderCreator.createValidGender()));

        BDDMockito.when(genderServiceMock.save(ArgumentMatchers.any(GenderPostRequestBody.class)))
            .thenReturn(GenderCreator.createValidGender());

        BDDMockito.doNothing()
            .when(genderServiceMock)
            .delete(ArgumentMatchers.any(UUID.class));

        BDDMockito.doNothing()
            .when(genderServiceMock)
            .replace(ArgumentMatchers.any(GenderPutRequestBody.class));
    }

    @Test
    @DisplayName("list returns list of author when successful")
    void list_ReturnsListOfAuthor_WhenSuccessful() {
        Gender validGender = GenderCreator.createValidGender();

        List<Gender> genders = genderController.list().getBody();

        assertThat(genders)
            .isNotEmpty()
            .hasSize(1)
            .contains(validGender);
    }

    @Test
    @DisplayName("list returns empty list when there are no authors")
    void list_ReturnsEmptyList_WhenThereAreNoAuthors() {
        BDDMockito.when(genderServiceMock.list())
            .thenReturn(Collections.emptyList());

        List<Gender> genders = genderController.list().getBody();

        assertThat(genders)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("save returns gender when successful")
    void save_ReturnsGender_WhenSuccessful() {
        Gender validGender = GenderCreator.createValidGender();

        Gender savedGender = genderController
            .save(GenderPostRequestBodyCreator.createGenderPostRequestBodyToBeSave(), null).getBody();

        assertThat(savedGender)
            .isNotNull()
            .isEqualTo(validGender);
    }

    @Test
    @DisplayName("delete removes gender when successful")
    void delete_RemovesGender_WhenSuccessful() {
        ResponseEntity<Void> entity = genderController
            .delete(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates gender when successful")
    void replace_UpdatesGender_WhenSuccessful() {
        ResponseEntity<Void> entity = genderController.replace(GenderPutRequestBodyCreator
            .createGenderPutRequestBodyToBeUpdate());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

}
