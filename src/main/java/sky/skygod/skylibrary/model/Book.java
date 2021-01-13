package sky.skygod.skylibrary.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Book extends BaseEntity {

    @NotEmpty(message = "Book name cannot be empty")
    private String name;

    @NotNull(message = "Book pages cannot be null")
    @Size(min = 10, message = "Book must have at least 10 pages")
    private Integer pages;

    @NotNull(message = "Book status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns =
    @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @NotNull
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "gender_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns =
    @JoinColumn(name = "gender_id"))
    private Set<Gender> genders;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "publishing_company_id")
    private PublishingCompany publishingCompany;

    private Long isbn;
    private String cover;
    private String description;

}
