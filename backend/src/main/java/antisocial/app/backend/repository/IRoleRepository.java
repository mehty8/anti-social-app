package antisocial.app.backend.repository;

import antisocial.app.backend.data.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(value = "select * from role_entity where role_name = :roleName ", nativeQuery = true)
    Optional<RoleEntity> findByRoleName(@Param("roleName") String roleName);
}
