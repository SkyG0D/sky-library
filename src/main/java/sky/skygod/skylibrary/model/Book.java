package sky.skygod.skylibrary.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include()
    private UUID uuid;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotEmpty(message = "Book name cannot be empty")
    @NotNull(message = "Book name cannot be null")
    private String name;

    @NotNull(message = "Book pages cannot be null")
    @Range(min = 10, message = "Book must have at least 10 pages")
    private Integer pages;

    @NotNull(message = "Book status cannot be null")
    @Enumerated(value = EnumType.STRING)
    @JsonEnumDefaultValue
    private Status status;

    @NotNull(message = "Book authors cannot be null")
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns =
    @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @NotNull(message = "Book genders cannot be null")
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "gender_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns =
    @JoinColumn(name = "gender_id"))
    private Set<Gender> genders;

    @NotNull(message = "Book publishing company cannot be null")
    @ManyToOne
    @JoinColumn(name = "publishing_company_id")
    private PublishingCompany publishingCompany;

    private String cover;
    private Long isbn;
    private String description;

}
