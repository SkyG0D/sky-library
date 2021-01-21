package sky.skygod.skylibrary.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PublishingCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include()
    private UUID uuid;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotEmpty(message = "Publishing company name cannot be empty")
    @NotNull(message = "Publishing company name cannot be null")
    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

}
