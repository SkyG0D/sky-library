package sky.skygod.skylibrary.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    List<Author> findByNameContainingIgnoreCase(String name);

}
