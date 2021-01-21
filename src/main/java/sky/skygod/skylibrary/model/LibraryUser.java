package sky.skygod.skylibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LibraryUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include()
    private UUID uuid;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotEmpty(message = "User name cannot be empty")
    @NotNull(message = "User name cannot be null")
    private String name;

    @Email(message = "Enter a valid email")
    @NotEmpty(message = "Email name cannot be empty")
    @NotNull(message = "User name cannot be null")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "User password cannot be empty")
    @NotNull(message = "User password cannot be null")
    @JsonIgnore
    private String password;

    @NotEmpty(message = "User authorities cannot be empty")
    @NotNull(message = "User authorities cannot be null")
    private String authorities;

    @Embedded
    private Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
            .stream(authorities.split(", "))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
