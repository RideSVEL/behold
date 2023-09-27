package serejka.telegram.behold.resources;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import serejka.telegram.behold.models.Review;
import serejka.telegram.behold.service.ReviewService;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewResource {

  ReviewService reviewService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/new")
  public List<Review> getNewReviews() {
    return reviewService.getNewReviews();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/archive")
  public List<Review> getArchiveReviews() {
    return reviewService.getArchiveReviews();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Review> findAllReviews() {
    return reviewService.findAllReviews();
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Review updateReview(@RequestBody Review review) {
    return reviewService.save(review);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/delete")
  public void deleteReview(@RequestBody Review review) {
    reviewService.deleteReview(review);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/count")
  public Long countReviews(@RequestParam(required = false) Integer view) {
    return view != null ? reviewService.countReviewsByView(view) : reviewService.countReviews();
  }
}
