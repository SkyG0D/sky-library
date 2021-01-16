package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class LibraryUserController {

    private final LibraryUserDetailsService libraryUserDetailsService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<?> list(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(libraryUserDetailsService.list(pageable, userDetails).get());
    }

    @GetMapping("/find-by")
    public ResponseEntity<?> findBy(@RequestParam String name, Pageable pageable,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(libraryUserDetailsService.findBy(name, pageable, userDetails).get());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok(libraryUserDetailsService.findByIdOrElseThrowNotFoundException(uuid, userDetails).get());
    }

    @PostMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<LibraryUserAdminGetResponseBody> save(
            @RequestBody @Valid LibraryUserPostRequestBody libraryUserPostRequestBody,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletResponse response) {

        LibraryUserAdminGetResponseBody savedUser = libraryUserDetailsService
                .save(libraryUserPostRequestBody, userDetails);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedUser.getUuid()));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid, @AuthenticationPrincipal UserDetails userDetails) {
        libraryUserDetailsService.delete(uuid, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> replace(@RequestBody @Valid LibraryUserPutRequestBody libraryUserPutRequestBody,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        libraryUserDetailsService.replace(libraryUserPutRequestBody, userDetails);
        return ResponseEntity.noContent().build();
    }

}
