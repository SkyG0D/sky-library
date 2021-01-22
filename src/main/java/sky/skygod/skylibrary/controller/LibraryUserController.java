package sky.skygod.skylibrary.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Api(value = "Library User")
public class LibraryUserController {

    private final LibraryUserDetailsService libraryUserDetailsService;
    private final ApplicationEventPublisher publisher;

    @ApiOperation(value = "Returns page object of library user")
    @GetMapping
    public ResponseEntity<?> list(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(libraryUserDetailsService.list(pageable, authentication).get());
    }

    @ApiOperation(value = "Returns library user given uuid")
    @GetMapping("/find-by")
    public ResponseEntity<?> findBy(@RequestParam String name, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity
            .ok(libraryUserDetailsService.findBy(name, pageable, authentication).get());
    }

    @ApiOperation(value = "Returns library user given uuid")
    @GetMapping("/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(libraryUserDetailsService
            .findByIdOrElseThrowNotFoundException(uuid, authentication).get());
    }

    @ApiOperation(value = "Creates new library user")
    @PostMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<LibraryUserAdminGetResponseBody> save(
        @RequestBody @Valid LibraryUserPostRequestBody libraryUserPostRequestBody,
        HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LibraryUserAdminGetResponseBody savedUser = libraryUserDetailsService
            .save(libraryUserPostRequestBody, authentication);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedUser.getUuid()));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Removes library user given uuid")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        libraryUserDetailsService.delete(uuid, authentication);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Updates library user")
    @PutMapping("/admin")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> replace(@RequestBody @Valid LibraryUserPutRequestBody libraryUserPutRequestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        libraryUserDetailsService.replace(libraryUserPutRequestBody, authentication);
        return ResponseEntity.noContent().build();
    }

}
