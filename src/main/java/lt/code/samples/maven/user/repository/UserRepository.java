package lt.code.samples.maven.user.repository;

import java.util.Optional;


import lt.code.samples.maven.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
