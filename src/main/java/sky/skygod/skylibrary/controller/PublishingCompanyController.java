package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.dto.publishingcompany.PublishingCompanyPutRequestBody;
import sky.skygod.skylibrary.event.ResourceCreatedEvent;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.service.PublishingCompanyService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublishingCompanyController {

    private final PublishingCompanyService publishingCompanyService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<Page<PublishingCompany>> list(Pageable pageable) {
        return ResponseEntity.ok(publishingCompanyService.list(pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PublishingCompany> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(publishingCompanyService.findByIdOrElseThrowNotFoundException(uuid));
    }

    @GetMapping("/findBy")
    public ResponseEntity<List<PublishingCompany>> findBy(@RequestParam String name) {
        return ResponseEntity.ok(publishingCompanyService.findBy(name));
    }

    @PostMapping("/admin")
    public ResponseEntity<PublishingCompany> save(
            @RequestBody @Valid PublishingCompanyPostRequestBody publishingCompanyPostRequestBody,
            HttpServletResponse response) {

        PublishingCompany savedPublishingCompany = publishingCompanyService.save(publishingCompanyPostRequestBody);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPublishingCompany.getUuid()));
        return new ResponseEntity<>(savedPublishingCompany, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        publishingCompanyService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin")
    public ResponseEntity<Void> replace(
            @RequestBody @Valid PublishingCompanyPutRequestBody publishingCompanyPutRequestBody) {

        publishingCompanyService.replace(publishingCompanyPutRequestBody);
        return ResponseEntity.noContent().build();
    }

}
