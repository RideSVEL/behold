package serejka.telegram.behold.config;

import info.movito.themoviedbapi.TmdbApi;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import serejka.telegram.behold.config.properties.BotProperties;
import serejka.telegram.behold.logic.bot.Bot;
import serejka.telegram.behold.logic.bot.Facade;

@Configuration
@RequiredArgsConstructor
public class BotConfig {

  private final BotProperties botProperties;

  @Bean
  public Bot SuperBot(Facade facade) {
    return new Bot(botProperties, facade);
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public TmdbApi tmdbApi() {
    return new TmdbApi("948e6670159df009cc3be4b3cbab0697");
  }
}
