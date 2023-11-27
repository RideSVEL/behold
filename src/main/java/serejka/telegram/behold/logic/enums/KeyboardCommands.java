package serejka.telegram.behold.logic.enums;

public enum KeyboardCommands {
  TOPDAY("Новинки\uD83C\uDD95"), TOPWEEK("TOP Тижня\uD83D\uDE0E"),
  TOP("TOP\uD83D\uDD25"), HELP("Допомога\uD83C\uDD98"),
  REVIEW("Залишити відгук\uD83D\uDE4B\u200D♂️"), CANCEL("Повернутись\uD83D\uDE15"),
  SEARCH("Пошук\uD83D\uDD0D"), RANDOM("Кінорулетка\uD83C\uDFB2"),
  BOOKMARKS("Закладки\uD83D\uDCBC"), DESCRIPTION("Вгадаю фільм\uD83E\uDEAC"),
  PERSONAL_LIST("Персональний ТОП\uD83D\uDD75\uFE0F\u200D♂\uFE0F");

  private final String value;

  KeyboardCommands(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
