package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.model.PublishingCompany;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPostRequestBody;
import sky.skygod.skylibrary.requests.publishingcompany.PublishingCompanyPutRequestBody;
import sky.skygod.skylibrary.service.PublishingCompanyService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublishingCompanyController {

    private final PublishingCompanyService publishingCompanyService;

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

    @PostMapping
    public ResponseEntity<PublishingCompany> save(
            @RequestBody @Valid PublishingCompanyPostRequestBody publishingCompanyPostRequestBody) {

        return ResponseEntity.ok(publishingCompanyService.save(publishingCompanyPostRequestBody));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        publishingCompanyService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> replace(
            @RequestBody @Valid PublishingCompanyPutRequestBody publishingCompanyPutRequestBody) {

        publishingCompanyService.replace(publishingCompanyPutRequestBody);
        return ResponseEntity.noContent().build();
    }

}
