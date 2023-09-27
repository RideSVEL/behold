package serejka.telegram.behold.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import serejka.telegram.behold.logic.bot.Bot;
import serejka.telegram.behold.models.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomMessageService {

  private final UserService userService;
  private final Bot bot;

  public void sendCustomMessageToUser(long chatId, String reply) {
    bot.sendMessageByAdmin(chatId, reply);
    log.info("Send message to user - {}, with text: {}", chatId, reply);
  }

  @SneakyThrows
  @Async
  public void messageToAllUsers(String reply) {
    List<User> allUsers = userService.findAllUsers();
    for (User user : allUsers) {
      Thread.sleep(50);
      try {
        bot.sendMessageByAdmin(user.getUserId(), reply);
        log.info("Send message to user - {}, with text: {}", user.getUserId(), reply);
      } catch (Exception e) {
        log.error("Chat not found");
      }
    }
  }
}
