package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.dto.user.LibraryUserAdminGetResponseBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPostRequestBody;
import sky.skygod.skylibrary.dto.user.LibraryUserPutRequestBody;
import sky.skygod.skylibrary.event.ResourceCreatedEvent;
import sky.skygod.skylibrary.service.LibraryUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Log4j2
public class LibraryUserController {

    private final LibraryUserDetailsService libraryUserDetailsService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<?> list(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(libraryUserDetailsService.list(pageable, authentication.getAuthorities()).get());
    }

    @GetMapping("/find-by")
    public ResponseEntity<?> findBy(@RequestParam String name, Pageable pageable,
                                    Authentication authentication) {
        return ResponseEntity
                .ok(libraryUserDetailsService.findBy(name, pageable, authentication.getAuthorities()).get());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid, Authentication authentication) {
        return ResponseEntity.ok(libraryUserDetailsService
                .findByIdOrElseThrowNotFoundException(uuid, authentication.getAuthorities()).get());
    }

    @PostMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<LibraryUserAdminGetResponseBody> save(
            @RequestBody @Valid LibraryUserPostRequestBody libraryUserPostRequestBody,
            Authentication authentication,
            HttpServletResponse response) {

        LibraryUserAdminGetResponseBody savedUser = libraryUserDetailsService
                .save(libraryUserPostRequestBody, authentication.getAuthorities());
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedUser.getUuid()));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid, Authentication authentication) {
        libraryUserDetailsService.delete(uuid, authentication.getAuthorities());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> replace(@RequestBody @Valid LibraryUserPutRequestBody libraryUserPutRequestBody,
                                        Authentication authentication) {

        libraryUserDetailsService.replace(libraryUserPutRequestBody, authentication.getAuthorities());
        return ResponseEntity.noContent().build();
    }

}
