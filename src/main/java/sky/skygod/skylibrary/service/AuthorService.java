package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.exception.NotFoundException;
import sky.skygod.skylibrary.mapper.AuthorMapper;
import sky.skygod.skylibrary.model.Author;
import sky.skygod.skylibrary.repository.author.AuthorRepository;
import sky.skygod.skylibrary.requests.author.AuthorPostRequestBody;
import sky.skygod.skylibrary.requests.author.AuthorPutRequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Page<Author> list(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author findByIdOrElseThrowNotFoundException(UUID uuid) {
        return authorRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Author not found"));
    }

    public Author save(AuthorPostRequestBody authorPostRequestBody) {
        return authorRepository.save(AuthorMapper.INSTANCE.toAuthor(authorPostRequestBody));
    }

    public void delete(UUID uuid) {
        authorRepository.delete(findByIdOrElseThrowNotFoundException(uuid));
    }

    public void replace(AuthorPutRequestBody authorPutRequestBody) {
        Author savedAuthor = findByIdOrElseThrowNotFoundException(authorPutRequestBody.getUuid());
        Author author = AuthorMapper.INSTANCE.toAuthor(authorPutRequestBody);
        author.setUuid(author.getUuid());
        authorRepository.save(author);
    }

    public List<Author> findBy(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

}
