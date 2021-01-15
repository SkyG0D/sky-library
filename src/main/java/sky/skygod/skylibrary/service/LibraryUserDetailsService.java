package sky.skygod.skylibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.skygod.skylibrary.repository.user.LibraryUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryUserDetailsService implements UserDetailsService {

    private final LibraryUserRepository libraryUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(libraryUserRepository.findByEmail(username))
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not found with email: \"" + username + "\".")
                );
    }

}
