package serejka.telegram.behold.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serejka.telegram.behold.models.Audit;
import serejka.telegram.behold.models.Liked;
import serejka.telegram.behold.models.User;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
  List<Audit> findByUserId(Long userId);
}
