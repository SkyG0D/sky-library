package sky.skygod.skylibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.repository.gender.GenderRepository;
import sky.skygod.skylibrary.util.gender.GenderCreator;
import sky.skygod.skylibrary.util.gender.GenderPostRequestBodyCreator;
import sky.skygod.skylibrary.util.gender.GenderPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(SpringExtension.class)
class GenderServiceTest {

    @InjectMocks
    private GenderService genderService;

    @Mock
    private GenderRepository genderRepositoryMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(genderRepositoryMock.findAll())
            .thenReturn(List.of(GenderCreator.createValidGender()));

        BDDMockito.when(genderRepositoryMock.save(ArgumentMatchers.any(Gender.class)))
            .thenReturn(GenderCreator.createValidGender());

        BDDMockito.doNothing()
            .when(genderRepositoryMock)
            .delete(ArgumentMatchers.any(Gender.class));

        BDDMockito.when(genderRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.ofNullable(GenderCreator.createValidGender()));
    }

    @Test
    @DisplayName("list returns list of gender when successful")
    void list_ReturnsListOfGender_WhenSuccessful() {
        Gender validGender = GenderCreator.createValidGender();

        List<Gender> genders = genderService.list();

        assertThat(genders)
            .isNotEmpty()
            .hasSize(1)
            .contains(validGender);
    }

    @Test
    @DisplayName("list returns empty list when there are no genders")
    void list_ReturnsEmptyList_WhenThereAreNoGenders() {
        BDDMockito.when(genderRepositoryMock.findAll())
            .thenReturn(Collections.emptyList());

        List<Gender> genders = genderService.list();

        assertThat(genders)
            .isNotNull()
            .isEmpty();
    }


    @Test
    @DisplayName("save returns gender when successful")
    void save_ReturnsGender_WhenSuccessful() {
        Gender validGender = GenderCreator.createValidGender();

        Gender savedGender = genderService
            .save(GenderPostRequestBodyCreator.createGenderPostRequestBodyToBeSave());

        assertThat(savedGender)
            .isNotNull()
            .isEqualTo(validGender);
    }

    @Test
    @DisplayName("delete removes gender when successful")
    void delete_RemovesGender_WhenSuccessful() {
        assertThatCode(() -> genderService.delete(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871")))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace updates gender when successful")
    void replace_UpdatesGender_WhenSuccessful() {
        BDDMockito.when(genderRepositoryMock.save(ArgumentMatchers.any(Gender.class)))
            .thenReturn(GenderCreator.createValidUpdatedGender());

        assertThatCode(() -> genderService.replace(
            GenderPutRequestBodyCreator.createGenderPutRequestBodyToBeUpdate()
        )).doesNotThrowAnyException();
    }

}
