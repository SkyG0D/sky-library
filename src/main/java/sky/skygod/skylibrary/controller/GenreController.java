package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sky.skygod.skylibrary.model.Gender;
import sky.skygod.skylibrary.service.GenderService;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenderService genderService;

    @PostMapping
    private ResponseEntity<Gender> save(@RequestBody Gender gender) {
        return new ResponseEntity<>(genderService.save(gender), HttpStatus.CREATED);
    }

}
