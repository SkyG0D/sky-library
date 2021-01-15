package sky.skygod.skylibrary.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.LibraryUser;

import java.util.UUID;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, UUID> {

    LibraryUser findByEmail(String email);

}
