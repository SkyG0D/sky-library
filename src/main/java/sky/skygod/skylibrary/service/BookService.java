package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

}
