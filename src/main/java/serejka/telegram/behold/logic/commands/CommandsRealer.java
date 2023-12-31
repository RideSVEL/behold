package serejka.telegram.behold.logic.commands;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.cache.UserDataCache;
import serejka.telegram.behold.logic.enums.BotState;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.service.AuditService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandsRealer {

  Map<Commands, MessageCommand> messageCommandMap;
  Map<CallbackCommands, CallbackCommand> callbackCommandMap;
  Map<BotState, StateCommand> stateCommandMap;
  UserDataCache userDataCache;
  AuditService auditService;

  @Autowired
  public CommandsRealer(
      List<MessageCommand> messageCommands,
      List<CallbackCommand> callbackCommands,
      List<StateCommand> stateCommands, UserDataCache userDataCache, AuditService auditService) {
    messageCommandMap = messageCommands.stream()
        .collect(toMap(MessageCommand::getMyCommand, Function.identity()));
    callbackCommandMap = callbackCommands.stream()
        .collect(toMap(CallbackCommand::getMyCommand, Function.identity()));
    stateCommandMap = stateCommands.stream()
        .collect(toMap(StateCommand::getMyCommand, Function.identity()));
    this.userDataCache = userDataCache;
    this.auditService = auditService;
  }

  public SendMessage answerOnUpdate(Message message) {
    return userDataCache.checkContainsKey(message.getFrom().getId()) ?
        sendMessageByStateUser(message) :
        sendMessageByIncomeMessage(message);
  }

  public SendMessage answerOnUpdate(CallbackQuery callbackQuery) {
    String[] data = callbackQuery.getData().split("=");
    log.info(callbackQuery.getData());
    CallbackCommands command = CallbackCommands.getName(data[0]);
    CallbackCommand callbackCommand = callbackCommandMap.get(command);
    if (callbackCommand == null) {
      log.error("Command not found - " + command);
      callbackCommand = callbackCommandMap.get(CallbackCommands.OTHER);
    }
    auditService.auditAction(callbackQuery.getFrom().getId(), Long.parseLong(data[1]), command.name());
    return callbackCommand.generateMessage(callbackQuery, data[1]);
  }

  private SendMessage sendMessageByStateUser(Message message) {
    return stateCommandMap
        .get(userDataCache.getUserBotState(message.getFrom().getId()))
        .generateMessage(message);
  }

  private SendMessage sendMessageByIncomeMessage(Message message) {
    return messageCommandMap
        .get(Commands.getName(message.getText()))
        .generateMessage(message);
  }
}
