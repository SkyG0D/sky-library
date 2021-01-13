package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.requests.author.AuthorPostRequestBody;
import sky.skygod.skylibrary.requests.author.AuthorPutRequestBody;
import sky.skygod.skylibrary.service.AuthorService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<Author>> list(Pageable pageable) {
        return ResponseEntity.ok(authorService.list(pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Author> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(authorService.findByIdOrElseThrowNotFoundException(uuid));
    }

    @GetMapping("/findBy")
    public ResponseEntity<List<Author>> findBy(@RequestParam String name) {
        return ResponseEntity.ok(authorService.findBy(name));
    }

    @PostMapping
    public ResponseEntity<Author> save(@RequestBody @Valid AuthorPostRequestBody authorPostRequestBody) {
        return ResponseEntity.ok(authorService.save(authorPostRequestBody));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        authorService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid AuthorPutRequestBody authorPutRequestBody) {
        authorService.replace(authorPutRequestBody);
        return ResponseEntity.noContent().build();
    }

}
