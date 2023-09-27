package serejka.telegram.behold.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIMovieResponse {
  @JsonProperty(value = "movie_name", required = true)
  private String name;
  @JsonProperty(value = "movie_year")
  private Integer year;
  @JsonProperty(value = "movie_tmdb_id")
  private Long tmdbId;
}
