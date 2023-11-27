package serejka.telegram.behold.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import serejka.telegram.behold.models.Audit;
import serejka.telegram.behold.repository.AuditRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditService {

  AuditRepository auditRepository;
  UserService userService;

  public void auditAction(Long userId, Long movieId, String action) {
    Audit auditRecord = Audit.builder()
        .action(action)
        .user(userService.findUserByUserId(userId))
        .movieId(movieId)
        .build();
    auditRepository.save(auditRecord);
  }
}
