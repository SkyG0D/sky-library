package sky.skygod.skylibrary.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import sky.skygod.skylibrary.model.LibraryUser;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;
import sky.skygod.skylibrary.util.user.LibraryUserCreator;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests for Library User Repository")
class LibraryUserRepositoryTest {

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Test
    @DisplayName("save persists library user when successful")
    void save_PersistsLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        assertThat(savedLibraryUser).isNotNull();

        assertThat(savedLibraryUser.getUuid()).isNotNull();

        assertThat(savedLibraryUser.getName())
            .isNotNull()
            .isEqualTo(libraryUser.getName());

        assertThat(savedLibraryUser.getEmail())
            .isNotNull()
            .isEqualTo(libraryUser.getEmail());
    }

    @Test
    @DisplayName("save updates library user when successful")
    void save_UpdatesLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        savedLibraryUser.setName("Silvano Marques");

        LibraryUser updatedLibraryUser = libraryUserRepository.save(savedLibraryUser);

        assertThat(updatedLibraryUser).isNotNull();

        assertThat(updatedLibraryUser.getUuid())
            .isNotNull()
            .isEqualTo(savedLibraryUser.getUuid());

        assertThat(updatedLibraryUser.getName())
            .isNotNull()
            .isEqualTo(savedLibraryUser.getName());
    }

    @Test
    @DisplayName("delete removes library user when successful")
    void delete_RemovesLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        libraryUserRepository.delete(savedLibraryUser);

        Optional<LibraryUser> optionalGender = libraryUserRepository.findById(savedLibraryUser.getUuid());

        assertThat(optionalGender).isEmpty();
    }

    @Test
    @DisplayName("findByNameIgnoreCaseContaining returns page of library user when successful")
    void findByNameIgnoreCaseContaining_ReturnsPageOfLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        Page<LibraryUser> libraryUsers = libraryUserRepository
            .findByNameIgnoreCaseContaining(savedLibraryUser.getName(), null);

        assertThat(libraryUsers).isNotNull();

        assertThat(libraryUsers.getContent())
            .hasSize(1)
            .contains(savedLibraryUser);

    }

    @Test
    @DisplayName("findByNameIgnoreCaseContaining returns empty page of library user when name not found")
    void findByNameIgnoreCaseContaining_ReturnsEmptyPageOfLibraryUser_WhenNameNotFound() {
        Page<LibraryUser> libraryUsers = libraryUserRepository
            .findByNameIgnoreCaseContaining("SkyG0D", null);

        assertThat(libraryUsers).isEmpty();
    }

    @Test
    @DisplayName("findByEmail returns library user when successful")
    void findByEmail_ReturnsLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        LibraryUser foundLibraryUser = libraryUserRepository.findByEmail(savedLibraryUser.getEmail());

        assertThat(foundLibraryUser)
            .isNotNull()
            .isEqualTo(savedLibraryUser);
    }

    @Test
    @DisplayName("findByEmail returns null when email not exists")
    void findByEmail_ReturnsNull_WhenEmailNotExists() {
        LibraryUser foundLibraryUser = libraryUserRepository.findByEmail("skyzin@hotmail.com");

        assertThat(foundLibraryUser).isNull();
    }

    @Test
    @DisplayName("findById returns library user when successful")
    void findById_ReturnsLibraryUser_WhenSuccessful() {
        LibraryUser libraryUser = LibraryUserCreator.createLibraryUserToBeSaved();

        LibraryUser savedLibraryUser = libraryUserRepository.save(libraryUser);

        Optional<LibraryUser> optionalLibraryUser = libraryUserRepository.findById(savedLibraryUser.getUuid());

        assertThat(optionalLibraryUser).isNotEmpty();

        assertThat(optionalLibraryUser.get()).isEqualTo(savedLibraryUser);
    }

    @Test
    @DisplayName("findById returns empty library user optional when uuid is invalid")
    void findById_ReturnsEmptyLibraryUserOptional_WhenUuidIsInvalid() {
        Optional<LibraryUser> optionalLibraryUser = libraryUserRepository
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4872"));

        assertThat(optionalLibraryUser).isEmpty();
    }

    @Test
    @DisplayName("save throws ConstraintViolationException when an required column is null")
    void save_ThrowsConstraintViolationException_WhenAnRequiredColumnIsNull() {
        LibraryUser libraryUser = new LibraryUser();

        assertThatThrownBy(() -> libraryUserRepository.saveAndFlush(libraryUser))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
