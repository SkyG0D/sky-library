package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.event.ResourceCreatedEvent;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.requests.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.requests.gender.GenderPutRequestBody;
import sky.skygod.skylibrary.service.GenderService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genders")
@RequiredArgsConstructor
public class GenderController {

    private final GenderService genderService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    private ResponseEntity<List<Gender>> list() {
        return ResponseEntity.ok(genderService.list());
    }

    @PostMapping
    private ResponseEntity<Gender> save(@RequestBody @Valid GenderPostRequestBody genderPostRequestBody,
                                        HttpServletResponse response) {
        Gender savedGender = genderService.save(genderPostRequestBody);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedGender.getUuid()));
        return new ResponseEntity<>(savedGender, HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuid}")
    private ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        genderService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    private ResponseEntity<Void> replace(@RequestBody @Valid GenderPutRequestBody genderPutRequestBody) {
        genderService.replace(genderPutRequestBody);
        return ResponseEntity.noContent().build();
    }
}