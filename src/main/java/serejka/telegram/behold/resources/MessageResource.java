package serejka.telegram.behold.resources;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import serejka.telegram.behold.service.CustomMessageService;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageResource {

  CustomMessageService customMessageService;

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
  public void customMessageToUser(
      @PathVariable(value = "id") Long id,
      @RequestBody String message) {
    customMessageService.sendCustomMessageToUser(id, message);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public void customMessageToAllUsers(@RequestBody String message) {
    customMessageService.messageToAllUsers(message);
  }
}
