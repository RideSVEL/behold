package serejka.telegram.behold.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.models.Stats;
import serejka.telegram.behold.repository.StatsRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  private final StatsRepository statsRepository;

  public List<Stats> findAllStatsCommand() {
    return statsRepository.findAll();
  }

  public void updateStatisticCommand(Message message) {
    Commands name = Commands.getName(message.getText());
    updateCountCommand(name);
  }

  public void updateCountCommand(Commands command) {
    Stats stats1;

    if (command != null) {
      Optional<Stats> stats = statsRepository.findByCommandName(command.name());
      stats1 = stats.orElseGet(
          Stats::new);
      stats1.setCommandName(command.name());
    } else {
      Optional<Stats> byCommandName
          = statsRepository.findByCommandName(Commands.OTHER.name());
      stats1 = byCommandName.orElseGet(
          Stats::new);
      stats1.setCommandName(Commands.OTHER.name());
    }
    stats1.setCount(stats1.getCount() + 1);
    statsRepository.save(stats1);
  }
}
