package antisocial.app.backend.repository;

import antisocial.app.backend.data.entity.PreassignedUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPreassignedUrlRepository extends JpaRepository<PreassignedUrlEntity, Long> {
}
