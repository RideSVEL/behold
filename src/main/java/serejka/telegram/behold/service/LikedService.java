package serejka.telegram.behold.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import serejka.telegram.behold.models.Liked;
import serejka.telegram.behold.repository.LikedRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikedService {

  LikedRepository likedRepository;
  UserService userService;

  public void updateLiked(Long userId, Long movieId, boolean isLiked) {
    var liked = likedRepository.findByMovieId(movieId)
        .orElse(new Liked());
    liked.setMovieId(movieId);
    liked.setUser(userService.findUserByUserId(userId));
    liked.setLiked(isLiked);
    likedRepository.save(liked);
  }
}
