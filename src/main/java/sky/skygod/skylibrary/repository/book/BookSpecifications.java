package sky.skygod.skylibrary.repository.book;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import sky.skygod.skylibrary.model.Book;
import sky.skygod.skylibrary.model.Status;

import java.util.UUID;

@Log4j2
public class BookSpecifications {

    public static Specification<Book> withName(String name) {
        return getSpec(name, (root, query, builder) -> builder.like(builder.lower(
                root.get("name")), like(name)));
    }

    public static Specification<Book> withDescription(String description) {
        return getSpec(description, (root, query, builder) -> builder.like(builder.lower(
                root.get("description")), like(description)));
    }

    public static Specification<Book> withStatus(Status status) {
        return getSpec(status, (root, query, builder) -> builder.equal(root.get("status"), status));
    }

    public static Specification<Book> withAuthorId(UUID authorId) {
        return getSpec(authorId, (root, query, builder) -> builder.equal(
                root.join("authors").get("uuid"), authorId));
    }

    public static Specification<Book> withAuthorName(String authorName) {
        return getSpec(authorName, (root, query, builder) -> builder.like(builder.lower(
                root.join("authors").get("name")), like(authorName)));
    }

    public static Specification<Book> withGenderId(UUID genderId) {
        return getSpec(genderId, (root, query, builder) -> builder.equal(
                root.join("genders").get("uuid"), genderId));
    }

    public static Specification<Book> withGenderName(String genderName) {
        return getSpec(genderName, (root, query, builder) -> builder.like(builder.lower(
                root.join("genders").get("name")), like(genderName)));
    }

    public static Specification<Book> withPublishingCompanyId(UUID publishingCompanyId) {
        return getSpec(publishingCompanyId, (root, query, builder) -> builder.equal(
                root.join("publishingCompany").get("uuid"), publishingCompanyId));
    }

    public static Specification<Book> withPublishingCompanyName(String publishingCompanyName) {
        return getSpec(publishingCompanyName, (root, query, builder) -> builder.like(builder.lower(
                root.join("publishingCompany").get("name")), like(publishingCompanyName)));
    }

    private static String like(String string) {
        return "%" + string.toLowerCase() + "%";
    }

    private static <T> Specification<Book> getSpec(T value, Specification<Book> spec) {
        return value != null ? spec : null;
    }
}
