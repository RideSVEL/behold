package serejka.telegram.behold.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import serejka.telegram.behold.models.Stats;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

  Optional<Stats> findByCommandName(String name);
}
