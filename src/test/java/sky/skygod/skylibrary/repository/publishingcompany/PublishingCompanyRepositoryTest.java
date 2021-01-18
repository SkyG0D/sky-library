package sky.skygod.skylibrary.repository.publishingcompany;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests for Publishing Company Repository")
@Log4j2
class PublishingCompanyRepositoryTest {

    @Autowired
    private PublishingCompanyRepository publishingCompanyRepository;

    @Test
    @DisplayName("save persists publishing company when successful")
    void save_PersistsPublishingCompany_WhenSuccessful() {
        PublishingCompany publishingCompany = PublishingCompanyCreator
            .createPublishingCompanyToBeSaved();

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(publishingCompany);

        assertThat(savedPublishingCompany).isNotNull();

        assertThat(savedPublishingCompany.getUuid()).isNotNull();

        assertThat(savedPublishingCompany.getName())
            .isNotNull()
            .isEqualTo(publishingCompany.getName());
    }

    @Test
    @DisplayName("save updates publishing company when successful")
    void save_UpdatesPublishingCompany_WhenSuccessful() {
        PublishingCompany publishingCompany = PublishingCompanyCreator
            .createPublishingCompanyToBeSaved();

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(publishingCompany);

        savedPublishingCompany.setName("Atlas SkyBook");

        PublishingCompany updatedPublishingCompany = publishingCompanyRepository.save(savedPublishingCompany);

        assertThat(updatedPublishingCompany).isNotNull();

        assertThat(updatedPublishingCompany.getUuid())
            .isNotNull()
            .isEqualTo(savedPublishingCompany.getUuid());

        assertThat(updatedPublishingCompany.getName())
            .isNotNull()
            .isEqualTo(savedPublishingCompany.getName());
    }

    @Test
    @DisplayName("delete removes publishing company when successful")
    void delete_RemovesPublishingCompany_WhenSuccessful() {
        PublishingCompany publishingCompany = PublishingCompanyCreator
            .createPublishingCompanyToBeSaved();

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(publishingCompany);

        publishingCompanyRepository.delete(savedPublishingCompany);

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository
            .findById(savedPublishingCompany.getUuid());

        assertThat(optionalPublishingCompany).isEmpty();
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase returns list of publishing company when successful")
    void findByNameContainingIgnoreCase_ReturnsListOfPublishingCompany_WhenSuccessful() {
        PublishingCompany publishingCompany = PublishingCompanyCreator
            .createPublishingCompanyToBeSaved();

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(publishingCompany);

        List<PublishingCompany> publishers = publishingCompanyRepository
            .findByNameContainingIgnoreCase(savedPublishingCompany.getName());

        assertThat(publishers)
            .isNotNull()
            .hasSize(1)
            .contains(savedPublishingCompany);
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase returns empty list when no publishing company is found")
    void findByNameContainingIgnoreCase_ReturnsEmptyList_WhenPublishingCompanyIsNotFound() {
        List<PublishingCompany> publishers = publishingCompanyRepository
            .findByNameContainingIgnoreCase("SkyG0D");

        assertThat(publishers).isEmpty();
    }

    @Test
    @DisplayName("findById returns publishing company when successful")
    void findById_ReturnsPublishingCompany_WhenSuccessful() {
        PublishingCompany publishingCompany = PublishingCompanyCreator
            .createPublishingCompanyToBeSaved();

        PublishingCompany savedPublishingCompany = publishingCompanyRepository.save(publishingCompany);

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository
            .findById(savedPublishingCompany.getUuid());

        assertThat(optionalPublishingCompany).isNotEmpty();

        assertThat(optionalPublishingCompany.get()).isEqualTo(savedPublishingCompany);
    }

    @Test
    @DisplayName("findById returns empty optional when uuid is invalid")
    void findById_ReturnsEmptyOptional_WhenUuidIsInvalid() {
        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4872"));

        assertThat(optionalPublishingCompany).isEmpty();
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        PublishingCompany publishingCompany = new PublishingCompany();

        assertThatThrownBy(() -> publishingCompanyRepository.saveAndFlush(publishingCompany))
            .isInstanceOf(ConstraintViolationException.class);
    }
}
