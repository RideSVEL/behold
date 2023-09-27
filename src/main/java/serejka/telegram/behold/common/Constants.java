package serejka.telegram.behold.common;

import java.util.HashMap;
import java.util.Map;

public class Constants {

  public static Map<Integer, String> getGenreMap() {
    var genres = new HashMap<Integer, String>();
    genres.put(28, "боевик");
    genres.put(12, "приключения");
    genres.put(16, "мультфильм");
    genres.put(35, "комедия");
    genres.put(80, "криминал");
    genres.put(99, "документальный");
    genres.put(18, "драма");
    genres.put(10751, "семейный");
    genres.put(14, "фэнтези");
    genres.put(36, "история");
    genres.put(27, "ужасы");
    genres.put(10402, "музыка");
    genres.put(9648, "детектив");
    genres.put(10749, "мелодрама");
    genres.put(878, "фантастика");
    genres.put(10770, "телевизионный фильм");
    genres.put(53, "триллер");
    genres.put(10752, "военный");
    genres.put(37, "вестерн");
    return genres;
  }
}
