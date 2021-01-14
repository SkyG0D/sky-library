package sky.skygod.skylibrary.repository.gender;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.Gender;

import java.util.UUID;

public interface GenderRepository extends JpaRepository<Gender, UUID> {

}
