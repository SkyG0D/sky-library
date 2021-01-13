package sky.skygod.skylibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.Book;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
