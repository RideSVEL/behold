package serejka.telegram.behold.logic.commands.callbackCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.behold.logic.commands.CallbackCommand;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.service.BookmarkService;
import serejka.telegram.behold.service.LikedService;
import serejka.telegram.behold.service.SendMessageService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikedCmd implements CallbackCommand {

  SendMessageService sendMsg;
  LikedService likedService;

  @Override
  public SendMessage generateMessage(CallbackQuery callbackQuery, String data) {
    likedService.updateLiked(callbackQuery.getFrom().getId(), Long.parseLong(data), true);
    return sendMsg.sendMsg(callbackQuery.getFrom().getId(), "Відзначено уподобаним\uD83D\uDC4C");
  }

  @Override
  public CallbackCommands getMyCommand() {
    return CallbackCommands.LIKED;
  }
}
