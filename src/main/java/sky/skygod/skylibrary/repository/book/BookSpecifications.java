package sky.skygod.skylibrary.repository.book;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.model.Status;

@Log4j2
public class BookSpecifications {

    public static Specification<Book> withName(String name) {
        if (name == null) {
            return null;
        }

        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Book> withDescription(String description) {
        if (description == null) {
            return null;
        }

        return (root, query, builder) -> builder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Book> withStatus(Status status) {
        if (status == null) {
            return null;
        }

        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

}
