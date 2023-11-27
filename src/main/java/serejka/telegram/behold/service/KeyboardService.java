package serejka.telegram.behold.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import serejka.telegram.behold.logic.enums.CallbackCommands;
import serejka.telegram.behold.logic.enums.Commands;
import serejka.telegram.behold.logic.enums.KeyboardCommands;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class KeyboardService {

  private static final Set<Commands> COMMANDS_WITH_CANCEL = Collections.unmodifiableSet(
      EnumSet.of(Commands.REVIEW, Commands.SEARCH, Commands.DESCRIPTION));

  BookmarkService bookmarkService;

  public SendMessage getKeyboard(long chatId, String text, Commands command) {
    return COMMANDS_WITH_CANCEL.contains(command)
        ? sendKeyboardWithMessage(chatId, text, createReturnKeyboard())
        : sendKeyboardWithMessage(chatId, text, createMainKeyboard());
  }

  private ReplyKeyboardMarkup createReturnKeyboard() {
    ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow row1 = new KeyboardRow();
    row1.add(new KeyboardButton("Повернутись\uD83D\uDE15"));
    keyboard.add(row1);
    replyKeyboardMarkup.setKeyboard(keyboard);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup createReplyMarkup() {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);
    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup createMainKeyboard() {
    ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow row1 = new KeyboardRow();
    row1.add(new KeyboardButton(KeyboardCommands.TOPDAY.getValue()));
    row1.add(new KeyboardButton(KeyboardCommands.TOPWEEK.getValue()));
    KeyboardRow row2 = new KeyboardRow();
    row2.add(new KeyboardButton(KeyboardCommands.TOP.getValue()));
    row2.add(new KeyboardButton(KeyboardCommands.HELP.getValue()));
    KeyboardRow row3 = new KeyboardRow();
    row3.add(new KeyboardButton(KeyboardCommands.RANDOM.getValue()));
    row3.add(new KeyboardButton(KeyboardCommands.BOOKMARKS.getValue()));
    KeyboardRow row4 = new KeyboardRow();
    row4.add(new KeyboardButton(KeyboardCommands.SEARCH.getValue()));
    row4.add(new KeyboardButton(KeyboardCommands.REVIEW.getValue()));
    KeyboardRow row5 = new KeyboardRow();
    row5.add(new KeyboardButton(KeyboardCommands.DESCRIPTION.getValue()));
    row5.add(new KeyboardButton(KeyboardCommands.PERSONAL_LIST.getValue()));
    keyboard.add(row1);
    keyboard.add(row2);
    keyboard.add(row5);
    keyboard.add(row3);
    keyboard.add(row4);

    replyKeyboardMarkup.setKeyboard(keyboard);
    return replyKeyboardMarkup;
  }

  private SendMessage sendKeyboardWithMessage(
      long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setParseMode("html");
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);
    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendMessage;
  }

  public InlineKeyboardMarkup getInlineMessageButtonForFilm(Long movieId, Long userId) {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(List.of(createBookmarkButton(movieId, userId)))
        .keyboardRow(List.of(createLikedButton(movieId, userId), createUnlikedButton(movieId, userId)))
        .build();
  }

  private InlineKeyboardButton createBookmarkButton(Long movieId, Long userId) {
    String text;
    String callback;

    if (bookmarkService.checkExistBookmark(userId, movieId).isEmpty()) {
      text = "Додати до закладок✅";
      callback = CallbackCommands.BOOKMARK.getValue() + "=" + movieId;
    } else {
      text = "Видалити з закладок❌";
      callback = CallbackCommands.DELETE_BOOKMARK.getValue() + "=" + movieId;
    }

    return InlineKeyboardButton.builder()
        .text(text)
        .callbackData(callback)
        .build();
  }

  public InlineKeyboardButton createLikedButton(Long movieId, Long userId) {
   return InlineKeyboardButton.builder()
       .text("Подобається\uD83D\uDE0D")
       .callbackData(CallbackCommands.LIKED.getValue() + "=" + movieId)
       .build();
  }

  public InlineKeyboardButton createUnlikedButton(Long movieId, Long userId) {
    return InlineKeyboardButton.builder()
        .text("Не раджу❌")
        .callbackData(CallbackCommands.UNLIKED.getValue() + "=" + movieId)
        .build();
  }
}
