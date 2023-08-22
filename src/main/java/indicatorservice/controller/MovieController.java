package indicatorservice.controller;

import indicatorservice.dto.AwardIntervalResponse;
import indicatorservice.exception.DataAlreadyExistsException;
import indicatorservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MovieController {
    @Autowired
    private MovieService movieService;

//    @Operation(summary = "Show Intervals")
//    @GetMapping("/award-intervals")
//    public AwardIntervalDTO getAwardIntervals() {
//        return movieService.getAwardIntervals();
//    }

    @Operation(summary = "Show Intervals")
    @GetMapping("/award-intervals")
    public AwardIntervalResponse getAwardIntervals() {
        return movieService.getAwardIntervals();
    }


    // Método manual é apenas uma alternativa / sugestão.
    @Operation(summary = "Load movies from CSV")
    @PostMapping("/movies/upload")
    public ResponseEntity<String> loadDataFromCSV() {
        boolean isDataLoaded = movieService.loadMoviesFromCSV();
        if (isDataLoaded) {
            return ResponseEntity.ok("Data loaded successfully");
        } else {
            throw new DataAlreadyExistsException("Data already exists in the database");
        }
    }
}
