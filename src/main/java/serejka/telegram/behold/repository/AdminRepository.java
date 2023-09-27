package serejka.telegram.behold.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.behold.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

  Optional<Admin> findByUsername(String username);
}
