package serejka.telegram.behold.config.properties;

import lombok.Data;
import org.jvnet.hk2.annotations.Service;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotProperties {
  private String webHookPath;
  private String botUsername;
  private String botToken;
}
