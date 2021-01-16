package sky.skygod.skylibrary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LibraryUser implements UserDetails {

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
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "User password cannot be empty")
    private String password;

    @NotEmpty(message = "User authorities cannot be empty")
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
