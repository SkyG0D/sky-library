package sky.skygod.skylibrary.repository.gender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.util.gender.GenderCreator;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests for Gender Repository")
class GenderRepositoryTest {

    @Autowired
    private GenderRepository genderRepository;

    @Test
    @DisplayName("save persists gender when successful")
    void save_PersistsGender_WhenSuccessful() {
        Gender gender = GenderCreator.createGenderToBeSaved();

        Gender savedGender = genderRepository.save(gender);

        assertThat(savedGender).isNotNull();

        assertThat(savedGender.getUuid()).isNotNull();

        assertThat(savedGender.getName())
            .isNotNull()
            .isEqualTo(gender.getName());
    }

    @Test
    @DisplayName("save updates gender when successful")
    void save_UpdatesGender_WhenSuccessful() {
        Gender gender = GenderCreator.createGenderToBeSaved();

        Gender savedGender = genderRepository.save(gender);

        savedGender.setName("Ação");

        Gender updatedGender = genderRepository.save(savedGender);

        assertThat(updatedGender).isNotNull();

        assertThat(updatedGender.getUuid())
            .isNotNull()
            .isEqualTo(savedGender.getUuid());

        assertThat(updatedGender.getName())
            .isNotNull()
            .isEqualTo(savedGender.getName());
    }

    @Test
    @DisplayName("delete removes gender when successful")
    void delete_RemovesGender_WhenSuccessful() {
        Gender gender = GenderCreator.createGenderToBeSaved();

        Gender savedGender = genderRepository.save(gender);

        genderRepository.delete(savedGender);

        Optional<Gender> optionalGender = genderRepository.findById(savedGender.getUuid());

        assertThat(optionalGender).isEmpty();
    }


    @Test
    @DisplayName("findById returns gender when successful")
    void findById_ReturnsGender_WhenSuccessful() {
        Gender gender = GenderCreator.createGenderToBeSaved();

        Gender savedGender = genderRepository.save(gender);

        Optional<Gender> optionalGender = genderRepository.findById(savedGender.getUuid());

        assertThat(optionalGender).isNotEmpty();

        assertThat(optionalGender.get()).isEqualTo(savedGender);
    }

    @Test
    @DisplayName("findById returns empty optional when uuid is invalid")
    void findById_ReturnsEmptyOptional_WhenUuidIsInvalid() {
        Optional<Gender> optionalGender = genderRepository
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4872"));

        assertThat(optionalGender).isEmpty();
    }

    @Test
    @DisplayName("save throws ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Gender gender = new Gender();

        assertThatThrownBy(() -> genderRepository.saveAndFlush(gender))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
