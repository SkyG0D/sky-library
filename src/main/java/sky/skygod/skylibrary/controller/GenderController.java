package sky.skygod.skylibrary.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.event.ResourceCreatedEvent;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.dto.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.dto.gender.GenderPutRequestBody;
import sky.skygod.skylibrary.service.GenderService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genders")
@RequiredArgsConstructor
@Api(value = "Gender")
public class GenderController {

    private final GenderService genderService;
    private final ApplicationEventPublisher publisher;

    @ApiOperation(value = "Returns list of gender")
    @GetMapping
    public ResponseEntity<List<Gender>> list() {
        return ResponseEntity.ok(genderService.list());
    }

    @ApiOperation(value = "Creates new gender")
    @PostMapping("/admin")
    public ResponseEntity<Gender> save(@RequestBody @Valid GenderPostRequestBody genderPostRequestBody,
                                        HttpServletResponse response) {
        Gender savedGender = genderService.save(genderPostRequestBody);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedGender.getUuid()));
        return new ResponseEntity<>(savedGender, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Removes gender given uuid")
    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        genderService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Updates gender")
    @PutMapping("/admin")
    public ResponseEntity<Void> replace(@RequestBody @Valid GenderPutRequestBody genderPutRequestBody) {
        genderService.replace(genderPutRequestBody);
        return ResponseEntity.noContent().build();
    }
}
