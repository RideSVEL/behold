package serejka.telegram.behold.logic.commands.callbackCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.behold.logic.commands.CallbackCommand;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.service.KeyboardService;
import serejka.telegram.behold.service.ReplyToUserService;
import serejka.telegram.behold.service.SendMessageService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieCmd implements CallbackCommand {
  ReplyToUserService replyToUserService;
  SendMessageService sendMsg;
  KeyboardService keyboardService;

  @Override
  public SendMessage generateMessage(CallbackQuery callbackQuery, String data) {
    String reply = replyToUserService.replyMovie(callbackQuery.getFrom().getId(), data);
    if (reply.equals("Что-то не получилось найти такой фильм...")) {
      return sendMsg.sendMsg(callbackQuery.getFrom().getId(), reply);
    }
    return sendMsg.sendMsg(callbackQuery.getFrom().getId(), reply,
        keyboardService.getInlineMessageButtonForFilm(Long.parseLong(data),
            callbackQuery.getFrom().getId()));
  }

  @Override
  public CallbackCommands getMyCommand() {
    return CallbackCommands.MOVIE;
  }
}
