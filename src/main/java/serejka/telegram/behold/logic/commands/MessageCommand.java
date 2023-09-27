package serejka.telegram.behold.logic.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.enums.Commands;

public interface MessageCommand {

  SendMessage generateMessage(Message message);

  Commands getMyCommand();
}
