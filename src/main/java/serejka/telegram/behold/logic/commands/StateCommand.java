package serejka.telegram.behold.logic.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.enums.BotState;

public interface StateCommand {

  SendMessage generateMessage(Message message);

  BotState getMyCommand();
}
