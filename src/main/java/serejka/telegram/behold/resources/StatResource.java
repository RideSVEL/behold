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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import serejka.telegram.behold.models.Stats;
import serejka.telegram.behold.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatResource {

  StatisticsService statisticsService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Stats> statisticsByCommands() {
    return statisticsService.findAllStatsCommand().stream()
        .sorted((Comparator.comparingInt(Stats::getCount)))
        .collect(Collectors.toList());
  }
}
