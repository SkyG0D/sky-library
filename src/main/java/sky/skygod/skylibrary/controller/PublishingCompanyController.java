package sky.skygod.skylibrary.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@Api(value = "Publishing Company")
public class PublishingCompanyController {

    private final PublishingCompanyService publishingCompanyService;
    private final ApplicationEventPublisher publisher;

    @ApiOperation(value = "Returns page object of publishing company")
    @GetMapping
    public ResponseEntity<Page<PublishingCompany>> list(Pageable pageable) {
        return ResponseEntity.ok(publishingCompanyService.list(pageable));
    }

    @ApiOperation(value = "Returns library user given uuid")
    @GetMapping("/{uuid}")
    public ResponseEntity<PublishingCompany> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(publishingCompanyService.findByIdOrElseThrowNotFoundException(uuid));
    }

    @ApiOperation(value = "Returns publishers list given parameter")
    @GetMapping("/find-by")
    public ResponseEntity<List<PublishingCompany>> findBy(@RequestParam String name) {
        return ResponseEntity.ok(publishingCompanyService.findBy(name));
    }

    @ApiOperation(value = "Creates new publishing company")
    @PostMapping("/admin")
    public ResponseEntity<PublishingCompany> save(
            @RequestBody @Valid PublishingCompanyPostRequestBody publishingCompanyPostRequestBody,
            HttpServletResponse response) {

        PublishingCompany savedPublishingCompany = publishingCompanyService.save(publishingCompanyPostRequestBody);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPublishingCompany.getUuid()));
        return new ResponseEntity<>(savedPublishingCompany, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Removes library user given uuid")
    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        publishingCompanyService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Removes library user")
    @PutMapping("/admin")
    public ResponseEntity<Void> replace(
            @RequestBody @Valid PublishingCompanyPutRequestBody publishingCompanyPutRequestBody) {

        publishingCompanyService.replace(publishingCompanyPutRequestBody);
        return ResponseEntity.noContent().build();
    }

}
