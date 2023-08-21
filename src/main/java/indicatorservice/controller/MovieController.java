package indicatorservice.controller;

import indicatorservice.dto.AwardIntervalDTO;
import indicatorservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Show Intervals")
    @GetMapping("/award-intervals")
    public AwardIntervalDTO getAwardIntervals() {
        return movieService.getAwardIntervals();
    }

    @Operation(summary = "Load movies from CSV")
    @PostMapping("/movies/upload")
    public ResponseEntity<String> loadDataFromCSV() {
        try {
            boolean isDataLoaded = movieService.loadMoviesFromCSV();
            if (isDataLoaded) {
                return ResponseEntity.ok("Data loaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Data already exists in the database");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load data: " + e.getMessage());
        }
    }
}
