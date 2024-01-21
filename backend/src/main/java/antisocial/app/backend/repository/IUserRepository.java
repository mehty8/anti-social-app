package antisocial.app.backend.repository;

import antisocial.app.backend.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from user_entity where username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);
}
