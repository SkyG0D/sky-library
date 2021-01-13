package sky.skygod.skylibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.skygod.skylibrary.model.PublishingCompany;

import java.util.List;
import java.util.UUID;

public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, UUID> {

    List<PublishingCompany> findByNameContainingIgnoreCase(String name);

}
