package serejka.telegram.behold.logic.bot;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import serejka.telegram.behold.config.properties.BotProperties;

@Service
@RequiredArgsConstructor
public class Bot extends TelegramWebhookBot {

  private final BotProperties properties;
  private final Facade facade;

  @SneakyThrows
  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    return facade.handle(update);
  }

  @Override
  public String getBotUsername() {
    return properties.getBotUsername();
  }

  @Override
  public String getBotToken() {
    return properties.getBotToken();
  }

  @Override
  public String getBotPath() {
    return properties.getWebHookPath();
  }

  @SneakyThrows
  public void sendMediaGroup(long chatId, List<InputMedia> inputMediaPhotos) {
    SendMediaGroup sendMediaGroup = new SendMediaGroup();
    sendMediaGroup.setMedias(inputMediaPhotos);
    sendMediaGroup.setChatId(String.valueOf(chatId));
    execute(sendMediaGroup);
  }

  @SneakyThrows
  public void sendMessageByAdmin(long chatId, String reply) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(String.valueOf(chatId));
    sendMessage.setParseMode("html");
    sendMessage.setText(reply);
    execute(sendMessage);
  }

  @SneakyThrows
  public void sendMessageWithKeyboard(
      long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setParseMode("html");
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);
    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    execute(sendMessage);
  }

  @SneakyThrows
  public void sendChatActionUpdate(long chatId, ActionType type) {
    SendChatAction sendChatAction = new SendChatAction();
    sendChatAction.setChatId(String.valueOf(chatId));
    sendChatAction.setAction(type);
    execute(sendChatAction);
  }

  @SneakyThrows
  public void sendDiceForWaiting(long chatId) {
    SendDice sendDice = new SendDice();
    sendDice.setChatId(chatId);
    execute(sendDice);
  }
}
