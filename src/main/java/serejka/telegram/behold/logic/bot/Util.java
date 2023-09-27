package serejka.telegram.behold.logic.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.service.StatisticsService;
import serejka.telegram.behold.service.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class Util {

  private final StatisticsService statisticsService;
  private final UserService userService;

  @Async
  public void updateStatisticsByCommand(Message message) {
    log.info(Thread.currentThread().getName());
    statisticsService.updateStatisticCommand(message);
    log.info("Done" + Thread.currentThread().getName());
  }

  @Async
  public void updateCountOfUse(Long userId) {
    log.info(Thread.currentThread().getName());
    userService.updateByUserIdCountOfUse(userId);
    log.info("Done" + Thread.currentThread().getName());
  }
}
