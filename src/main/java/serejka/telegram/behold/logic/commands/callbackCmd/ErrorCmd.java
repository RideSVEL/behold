package serejka.telegram.behold.logic.commands.callbackCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.behold.logic.commands.CallbackCommand;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.service.KeyboardService;
import serejka.telegram.behold.service.ReplyToUserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorCmd implements CallbackCommand {

  ReplyToUserService replyToUserService;
  KeyboardService keyboardService;

  @Override
  public SendMessage generateMessage(CallbackQuery callbackQuery, String data) {
    return keyboardService.getKeyboard(callbackQuery.getFrom().getId(),
        replyToUserService.replyError(), Commands.OTHER);
  }

  @Override
  public CallbackCommands getMyCommand() {
    return CallbackCommands.OTHER;
  }
}
