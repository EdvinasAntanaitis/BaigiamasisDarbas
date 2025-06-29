package lt.code.samples.maven.user.repository;

import lt.code.samples.maven.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.authorities WHERE u.email = :email")
    Optional<UserEntity> findUserByEmailWithAuthorities(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.authorities WHERE u.username = :username")
    Optional<UserEntity> findUserByUsernameWithAuthorities(@Param("username") String username);

    Optional<UserEntity> findByUsername(String username);
}
