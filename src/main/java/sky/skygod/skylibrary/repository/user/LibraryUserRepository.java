package sky.skygod.skylibrary.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.LibraryUser;

import java.util.UUID;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, UUID> {

    Page<LibraryUser> findByNameIgnoreCaseContaining(String name, Pageable pageable);

    LibraryUser findByEmail(String email);

}
