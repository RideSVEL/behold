package serejka.telegram.behold.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import serejka.telegram.behold.models.AIMovieResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

  @Value("${ai.token}")
  private String token;

  private static final String IN_JSON_SCHEME = "\nIn JSON Scheme format:\n";
  private static final String SEND_FILM_PREFIX = "Send me the name of the film in which ";

  public static final String IN_JSON_SCHEME_FULL = """
      In JSON Scheme format:
      {
        "$schema": "http://json-schema.org/draft-07/schema#",
        "title": "Generated schema for Root",
        "type": "array",
        "items": {
          "type": "object",
          "properties": {
            "movie_name": {
              "type": "string"
            },
            "movie_year": {
              "type": "number"
            },
            "movie_tmdb_id": {
              "type": "number"
            }
          },
          "required": [
            "movie_name"
          ]
        }
      }
      """;

  private OpenAiService openAiService;
  private String movieResponseJsonScheme;
  ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  @PostConstruct
  private void initSchema() {
    this.openAiService = new OpenAiService(token);
    JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(objectMapper);
    JsonSchema jsonSchema = schemaGenerator.generateSchema(AIMovieResponse.class);
    movieResponseJsonScheme = objectMapper.writeValueAsString(jsonSchema);
    log.info("Inited json scheme - {}", movieResponseJsonScheme);
  }

  public Optional<AIMovieResponse> getMovieResponse(String userDescription) {
    var completionRequest = buildCompletionRequest(userDescription);

    return getMessageContent(completionRequest)
        .map(this::getAiMovieResponse)
        .stream()
        .flatMap(Collection::stream)
        .findFirst();
  }

  private List<AIMovieResponse> getAiMovieResponse(String content) {
    try {
      return objectMapper.readValue(content, new TypeReference<>() {});
    } catch (Exception e) {
      log.error("Error occurred during parse the GPT response for content: {}", content, e);
      return Collections.emptyList();
    }
  }

  @NotNull
  private Optional<String> getMessageContent(ChatCompletionRequest completionRequest) {
    Optional<String> s = openAiService.createChatCompletion(completionRequest)
        .getChoices()
        .stream().findFirst()
        .map(ChatCompletionChoice::getMessage)
        .map(ChatMessage::getContent);
    s.ifPresent(System.out::println);
    return s;
  }

  private ChatCompletionRequest buildCompletionRequest(String userDescription) {
    return ChatCompletionRequest.builder()
        .model("gpt-3.5-turbo")
        .temperature(0.8)
        .messages(List.of(new ChatMessage(ChatMessageRole.USER.value(), buildGPTRequestMessage(userDescription))))
        .n(1)
        .maxTokens(500)
        .logitBias(new HashMap<>())
        .build();
  }

  private String buildGPTRequestMessage(String userDescription) {
    return SEND_FILM_PREFIX + userDescription + "/n" + IN_JSON_SCHEME_FULL;
  }
}
