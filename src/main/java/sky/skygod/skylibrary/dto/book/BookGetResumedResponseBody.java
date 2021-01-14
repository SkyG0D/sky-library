package sky.skygod.skylibrary.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sky.skygod.skylibrary.model.Status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookGetResumedResponseBody {

    private UUID uuid;
    private LocalDateTime createdAt;
    private String name;
    private Integer pages;
    private Status status;
    private Set<String> authors;
    private Set<String> genders;
    private String publishingCompany;
    private Long isbn;
    private String cover;
    private String description;


}
