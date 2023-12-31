package serejka.telegram.behold.logic.commands.stateCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.logic.commands.StateCommand;
import serejka.telegram.behold.logic.enums.BotState;
import serejka.telegram.behold.service.MovieService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchState implements StateCommand {

  MovieService movieService;

  @Override
  public SendMessage generateMessage(Message message) {
    return movieService.searchLogic(message);
  }

  @Override
  public BotState getMyCommand() {
    return BotState.SEARCH;
  }
}
