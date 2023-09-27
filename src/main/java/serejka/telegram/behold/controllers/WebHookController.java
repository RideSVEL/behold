package serejka.telegram.behold.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.behold.logic.bot.Bot;

@Slf4j
@RestController("/")
@RequiredArgsConstructor
public class WebHookController {

  private final Bot superBot;

  @PostMapping
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    log.info(String.valueOf(update));
    return superBot.onWebhookUpdateReceived(update);
  }
}
