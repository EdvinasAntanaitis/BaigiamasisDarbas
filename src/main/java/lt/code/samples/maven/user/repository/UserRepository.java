package lt.code.samples.maven.user.repository;

import lt.code.samples.maven.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.authorities WHERE u.username = :username")
    Optional<UserEntity> findUserByUsernameWithAuthorities(@Param("username") String username);

    Optional<UserEntity> findByUsername(String username);
}
