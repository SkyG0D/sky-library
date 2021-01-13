package sky.skygod.skylibrary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LibraryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email")
    @NotEmpty(message = "Email name cannot be empty")
    private String email;

    @NotEmpty(message = "User password cannot be empty")
    private String password;

    @NotEmpty(message = "User authorities cannot be empty")
    private String authorities;

    @Embedded
    private Address address;

}
