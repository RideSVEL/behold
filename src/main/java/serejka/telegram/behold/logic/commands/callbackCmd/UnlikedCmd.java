package serejka.telegram.behold.logic.commands.callbackCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.behold.logic.commands.CallbackCommand;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.service.LikedService;
import serejka.telegram.behold.service.SendMessageService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UnlikedCmd implements CallbackCommand {

  SendMessageService sendMsg;
  LikedService likedService;

  @Override
  public SendMessage generateMessage(CallbackQuery callbackQuery, String data) {
    likedService.updateLiked(callbackQuery.getFrom().getId(), Long.parseLong(data), false);
    return sendMsg.sendMsg(callbackQuery.getFrom().getId(), "Зазначено не рекомендувати\uD83D\uDE1E");
  }

  @Override
  public CallbackCommands getMyCommand() {
    return CallbackCommands.UNLIKED;
  }
}
