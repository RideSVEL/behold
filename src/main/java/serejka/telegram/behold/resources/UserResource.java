package serejka.telegram.behold.resources;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import serejka.telegram.behold.models.User;
import serejka.telegram.behold.repository.UserSearchRepository;
import serejka.telegram.behold.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserResource {

  UserService userService;
  UserSearchRepository userSearchRepository;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<User> getAllUsers() {
    return userService.findAllUsers().stream()
        .sorted((Comparator.comparingLong(User::getId)))
        .collect(Collectors.toList());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{page}/{count}/{sort}/{direction}")
  public List<User> usersPageable(
      @PathVariable Integer page,
      @PathVariable Integer count,
      @PathVariable String sort,
      @PathVariable Integer direction) {
    return userService.findAllUsersPageable(page, count, sort, direction);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/count")
  public Long countUsers() {
    return userService.countAllUsers();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/search")
  public List<User> searchUsers(@RequestParam String searchText) {
    return userSearchRepository.searchUsers(searchText);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
  public User findUserByUserId(@PathVariable Long id) {
    return userService.findUserByUserId(id);
  }
}
