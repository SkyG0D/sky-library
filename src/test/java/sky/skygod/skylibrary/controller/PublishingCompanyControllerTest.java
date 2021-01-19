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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPutRequestBody;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.service.PublishingCompanyService;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPostRequestBodyCreator;
import sky.skygod.skylibrary.util.publishingcompany.PublishingCompanyPutRequestBodyCreator;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class PublishingCompanyControllerTest {

    @InjectMocks
    private PublishingCompanyController publishingCompanyController;

    @Mock
    private PublishingCompanyService publishingCompanyServiceMock;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        PageImpl<PublishingCompany> publishingCompanyPage = new PageImpl<>(
            List.of(PublishingCompanyCreator.createValidPublishingCompany()));

        BDDMockito.doNothing()
            .when(publisher).publishEvent(ArgumentMatchers.any());

        BDDMockito.when(publishingCompanyServiceMock.list(ArgumentMatchers.any()))
            .thenReturn(publishingCompanyPage);

        BDDMockito
            .when(publishingCompanyServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenReturn(PublishingCompanyCreator.createValidPublishingCompany());

        BDDMockito.when(publishingCompanyServiceMock.findBy(ArgumentMatchers.anyString()))
            .thenReturn(List.of(PublishingCompanyCreator.createValidPublishingCompany()));

        BDDMockito.when(publishingCompanyServiceMock.save(ArgumentMatchers.any(PublishingCompanyPostRequestBody.class)))
            .thenReturn(PublishingCompanyCreator.createValidPublishingCompany());

        BDDMockito.doNothing()
            .when(publishingCompanyServiceMock)
            .delete(ArgumentMatchers.any(UUID.class));

        BDDMockito.doNothing()
            .when(publishingCompanyServiceMock)
            .replace(ArgumentMatchers.any(PublishingCompanyPutRequestBody.class));
    }

    @Test
    @DisplayName("list returns list of publishers inside page object when successful")
    void list_ReturnsListOfPublishersInsidePageObject_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        Page<PublishingCompany> publishingCompanyPage = publishingCompanyController.list(null).getBody();

        assertThat(publishingCompanyPage).isNotNull();

        assertThat(publishingCompanyPage.toList())
            .isNotEmpty()
            .hasSize(1)
            .contains(validPublishingCompany);
    }

    @Test
    @DisplayName("list returns empty page when there are no publishers")
    void list_ReturnsEmptyPage_WhenThereAreNoPublishers() {
        BDDMockito.when(publishingCompanyServiceMock.list(ArgumentMatchers.any()))
            .thenReturn(Page.empty());

        Page<PublishingCompany> publishingCompanyPage = publishingCompanyController.list(null).getBody();

        assertThat(publishingCompanyPage)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("findById returns a publishing company when successful")
    void findById_ReturnsPublishingCompany_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        PublishingCompany foundPublishingCompany = publishingCompanyController
            .findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871")).getBody();

        assertThat(foundPublishingCompany)
            .isNotNull()
            .isEqualTo(validPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns a list of publishers when successful")
    void findBy_ReturnsListOfPublishers_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        List<PublishingCompany> publishers = publishingCompanyController.findBy("SkyG0D").getBody();

        assertThat(publishers)
            .isNotEmpty()
            .hasSize(1)
            .contains(validPublishingCompany);
    }

    @Test
    @DisplayName("findBy returns empty list when publishers not found")
    void findBy_ReturnsEmptyList_WhenPublishersNotFound() {
        BDDMockito.when(publishingCompanyServiceMock.findBy(ArgumentMatchers.anyString()))
            .thenReturn(Collections.emptyList());

        List<PublishingCompany> publishers = publishingCompanyController.findBy("SkyG0D").getBody();

        assertThat(publishers)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("save returns publishing company when successful")
    void save_ReturnsPublishingCompany_WhenSuccessful() {
        PublishingCompany validPublishingCompany = PublishingCompanyCreator.createValidPublishingCompany();

        PublishingCompany savedPublishingCompany = publishingCompanyController
            .save(PublishingCompanyPostRequestBodyCreator
                .createPublishingCompanyPostRequestBodyToBeSaved(), null).getBody();

        assertThat(savedPublishingCompany)
            .isNotNull()
            .isEqualTo(validPublishingCompany);
    }

    @Test
    @DisplayName("delete removes publishing company when successful")
    void delete_RemovesPublishingCompany_WhenSuccessful() {
        ResponseEntity<Void> entity = publishingCompanyController
            .delete(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"));

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates publishing company when successful")
    void replace_UpdatesPublishingCompany_WhenSuccessful() {
        ResponseEntity<Void> entity = publishingCompanyController.replace(PublishingCompanyPutRequestBodyCreator
            .createPublishingCompanyPutRequestBodyToBeUpdate());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode())
            .isNotNull()
            .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("findById throws NotFoundException when publishing company does not exists")
    void findById_ThrowsNotFoundException_WhenPublishingDoesNotExists() {
        BDDMockito
            .when(publishingCompanyServiceMock.findByIdOrElseThrowNotFoundException(ArgumentMatchers.any(UUID.class)))
            .thenThrow(NotFoundException.class);

        assertThatThrownBy(
            () -> publishingCompanyController.findById(UUID.fromString("0714e7be-313c-4ca3-9665-20f7256e4871"))
        ).isInstanceOf(NotFoundException.class);
    }

}
