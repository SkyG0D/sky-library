package sky.skygod.skylibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.repository.publishingcompany.PublishingCompanyRepository;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPostRequestBodyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PublishingCompanyServiceTest {

    @InjectMocks
    private PublishingCompanyService publishingCompanyService;

    @Mock
    private PublishingCompanyRepository publishingCompanyRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<PublishingCompany> publishingCompanyPage = new PageImpl<>(
            List.of(PublishingCompanyCreator.createValidPublishingCompany()));

        BDDMockito.when(publishingCompanyRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(publishingCompanyPage);

        BDDMockito.when(publishingCompanyRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.ofNullable(PublishingCompanyCreator.createValidPublishingCompany()));

        BDDMockito.when(publishingCompanyRepositoryMock.findByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
            .thenReturn(List.of(PublishingCompanyCreator.createValidPublishingCompany()));

        BDDMockito
            .when(publishingCompanyRepositoryMock.save(ArgumentMatchers.any(PublishingCompany.class)))
            .thenReturn(PublishingCompanyCreator.createValidPublishingCompany());

        BDDMockito.doNothing()
            .when(publishingCompanyRepositoryMock)
            .delete(ArgumentMatchers.any(PublishingCompany.class));
    }

    @Test
    @DisplayName("list returns list of publishers inside page object when successful")
    void list_ReturnsListOfPublishersInsidePageObject_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        Page<PublishingCompany> publishingCompanyPage = publishingCompanyService.list(PageRequest.of(1, 1));

        assertThat(publishingCompanyPage)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(validPublishingCompany);
    }

    @Test
    @DisplayName("list returns empty page when there are no publishers")
    void list_ReturnsEmptyPage_WhenThereAreNoPublishers() {
        BDDMockito.when(publishingCompanyRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());

        Page<PublishingCompany> publishingCompanyPage = publishingCompanyService.list(PageRequest.of(1, 1));

        assertThat(publishingCompanyPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException returns a publishing company when successful")
    void findByIdOrElseThrowNotFoundException_ReturnsPublishingCompany_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        PublishingCompany foundPublishingCompany = publishingCompanyService
            .findByIdOrElseThrowNotFoundException(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"));

        assertThat(foundPublishingCompany)
            .isNotNull()
            .isEqualTo(validPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns a list of publishers when successful")
    void findBy_ReturnsListOfPublishers_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        List<PublishingCompany> publishers = publishingCompanyService.findBy("SkyG0D");

        assertThat(publishers)
            .isNotEmpty()
            .hasSize(1)
            .contains(validPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns empty list when publishers not found")
    void findBy_ReturnsEmptyList_WhenPublishersNotFound() {
        BDDMockito.when(publishingCompanyRepositoryMock.findByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
            .thenReturn(Collections.emptyList());

        List<PublishingCompany> publishers = publishingCompanyService.findBy("SkyG0D");

        assertThat(publishers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns publishing company when successful")
    void save_ReturnsPublishingCompany_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        PublishingCompany savedPublishingCompany = publishingCompanyService
            .save(PublishingCompanyPostRequestBodyCreator.createPublishingCompanyPostRequestBodyToBeSaved());

        assertThat(savedPublishingCompany)
            .isNotNull()
            .isEqualTo(validPublishingCompany);
    }

    @Test
    @DisplayName("delete removes publishing company when successful")
    void delete_RemovesPublishingCompany_WhenSuccessful() {
        assertThatCode(() -> publishingCompanyService.delete(UUID.fromString(
            "0714e7be-313c-4ca3-9665-20f7256e4871"))
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace updates publishing company when successful")
    void replace_UpdatesPublishingCompany_WhenSuccessful() {
       assertThatCode(() -> publishingCompanyService.replace(PublishingCompanyPutRequestBodyCreator
           .createPublishingCompanyPutRequestBodyToBeUpdate())
       ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("findByIdOrElseThrowNotFoundException throws NotFoundException when publishing company does not exists")
    void findByIdOrElseThrowNotFoundException_ThrowsNotFoundException_WhenPublishingDoesNotExists() {
        BDDMockito
            .when(publishingCompanyRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
            .thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> publishingCompanyService.findByIdOrElseThrowNotFoundException(
            UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
        ).isInstanceOf(NotFoundException.class);
    }

}
