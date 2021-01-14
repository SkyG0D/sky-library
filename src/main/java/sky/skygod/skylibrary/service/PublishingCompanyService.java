package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.mapper.PublishingCompanyMapper;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.repository.publishingcompany.PublishingCompanyRepository;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPutRequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;

    public Page<PublishingCompany> list(Pageable pageable) {
        return publishingCompanyRepository.findAll(pageable);
    }

    public PublishingCompany findByIdOrElseThrowNotFoundException(UUID uuid) {
        return publishingCompanyRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Publishing company not found"));
    }

    public List<PublishingCompany> findBy(String name) {
        return publishingCompanyRepository.findByNameContainingIgnoreCase(name);
    }

    public PublishingCompany save(PublishingCompanyPostRequestBody publishingCompanyPostRequestBody) {
        return publishingCompanyRepository
                .save(PublishingCompanyMapper.INSTANCE.toPublishingCompany(publishingCompanyPostRequestBody));
    }

    public void delete(UUID uuid) {
        publishingCompanyRepository.delete(findByIdOrElseThrowNotFoundException(uuid));
    }

    public void replace(PublishingCompanyPutRequestBody publishingCompanyPutRequestBody) {
        PublishingCompany savedPublishingCompany = findByIdOrElseThrowNotFoundException(
                publishingCompanyPutRequestBody.getUuid());
        PublishingCompany publishingCompany = PublishingCompanyMapper.INSTANCE
                .toPublishingCompany(publishingCompanyPutRequestBody);

        publishingCompany.setUuid(savedPublishingCompany.getUuid());
        publishingCompanyRepository.save(publishingCompany);
    }

}
