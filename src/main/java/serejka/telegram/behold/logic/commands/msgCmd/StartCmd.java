package serejka.telegram.behold.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.commands.MessageCommand;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.service.KeyboardService;
import serejka.telegram.behold.service.ReplyToUserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StartCmd implements MessageCommand {

  KeyboardService keyboardService;
  ReplyToUserService replyToUserService;

  @Override
  public SendMessage generateMessage(Message message) {
    return keyboardService.getKeyboard(message.getChatId(),
        replyToUserService.replyStart(message), Commands.START);
  }

  @Override
  public Commands getMyCommand() {
    return Commands.START;
  }
}
