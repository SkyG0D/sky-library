package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.skygod.skylibrary.event.ResourceCreatedEvent;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.repository.book.BookFilter;
import sky.skygod.skylibrary.dto.book.BookGetResumedResponseBody;
import sky.skygod.skylibrary.dto.book.BookPostRequestBody;
import sky.skygod.skylibrary.dto.book.BookPutRequestBody;
import sky.skygod.skylibrary.service.BookService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1//books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<Page<Book>> search(BookFilter bookFilter, Pageable pageable) {
        return ResponseEntity.ok(bookService.search(bookFilter, pageable));
    }

    @GetMapping(params = "resumed")
    public ResponseEntity<Page<BookGetResumedResponseBody>> searchResumed(BookFilter bookFilter, Pageable pageable) {
        return ResponseEntity.ok(bookService.resumed(bookFilter, pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Book> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(bookService.findByIdOrElseThrowNotFoundException(uuid));
    }

    @GetMapping("/by-isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable Long isbn) {
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @PostMapping("/admin")
    public ResponseEntity<Book> save(@RequestBody @Valid BookPostRequestBody bookPostRequestBody,
                                     HttpServletResponse response) {

        Book savedBook = bookService.save(bookPostRequestBody);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, savedBook.getUuid()));
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        bookService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin")
    public ResponseEntity<Void> replace(@RequestBody @Valid BookPutRequestBody bookPutRequestBody) {
        bookService.replace(bookPutRequestBody);
        return ResponseEntity.noContent().build();
    }

}
