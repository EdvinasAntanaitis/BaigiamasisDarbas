package lt.code.samples.maven.user.repository;

import java.util.Optional;


import lt.code.samples.maven.user.model.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    Optional<AuthorityEntity> findByName(String name);
}
