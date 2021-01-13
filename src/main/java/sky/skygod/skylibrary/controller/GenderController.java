package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.requests.gender.GenderPostRequestBody;
import sky.skygod.skylibrary.requests.gender.GenderPutRequestBody;
import sky.skygod.skylibrary.service.GenderService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genders")
@RequiredArgsConstructor
public class GenderController {

    private final GenderService genderService;

    @GetMapping
    private ResponseEntity<List<Gender>> list() {
        return ResponseEntity.ok(genderService.list());
    }

    @PostMapping
    private ResponseEntity<Gender> save(@RequestBody @Valid GenderPostRequestBody genderPostRequestBody) {
        return new ResponseEntity<>(genderService.save(genderPostRequestBody), HttpStatus.CREATED);
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
