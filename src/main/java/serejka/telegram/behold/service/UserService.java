package serejka.telegram.behold.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.behold.models.User;
import serejka.telegram.behold.repository.UserRepository;
import serejka.telegram.behold.repository.UserRepositoryImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserRepositoryImpl userRepositoryImpl;

  public void save(User user) {
    userRepository.save(user);
  }

  public boolean exists(Long id) {
    return userRepository.existsUserByUserId(id);
  }

  public List<User> findUsersByGreaterId(Long id, int limit) {
    return userRepositoryImpl.findOrderedByIdLimitingBy(id, limit);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public List<User> findAllUsersPageable(Integer page, Integer count, String sort, Integer direction) {
    Sort sorting = Sort.by(sort).ascending();
    if (direction == 1) {
      sorting = sorting.descending();
    }
    Pageable sortedByIdAsc =
        PageRequest.of(page, count, sorting);
    return userRepository.findAll(sortedByIdAsc).get().collect(Collectors.toList());
  }

  public void checkAndSave(Message message) {
    if (exists(message.getFrom().getId())) {
      log.info(" <||> User already exists!");
    } else {
      User user = new User(message.getFrom().getId(), message.getFrom().getUserName(),
          message.getFrom().getFirstName(), message.getFrom().getLastName(), 0);
      log.info(" <||> Save to DB User: {} ", user);
      save(user);
    }
  }

  public User findUserByUserId(Long userId) {
    return userRepository.findUserByUserId(userId);
  }

  public void updateByUserIdCountOfUse(Long userId) {
    try {
      User userByUserId = findUserByUserId(userId);
      userByUserId.setCountOfUse(userByUserId.getCountOfUse() + 1);
      save(userByUserId);
    } catch (NullPointerException e) {
      log.error("GET NPE FOR FIRST USER");
    }
  }

  public Long countAllUsers() {
    return userRepository.count();
  }
}
