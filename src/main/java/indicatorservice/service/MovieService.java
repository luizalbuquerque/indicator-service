package indicatorservice.service;

import indicatorservice.dto.AwardIntervalDTO;
import indicatorservice.dto.AwardIntervalResponse;
import indicatorservice.dto.ProducerIntervalDTO;
import indicatorservice.entity.MovieEntity;
import indicatorservice.exception.CsvLoadException;
import indicatorservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

//    public AwardIntervalDTO getAwardIntervals() {
//        List<MovieEntity> winningMovies = fetchWinningMovies();
//
//        Map<String, List<MovieEntity>> moviesGroupedByProducer = groupMoviesByProducer(winningMovies);
//
//        List<ProducerIntervalDTO> allIntervals = calculateIntervalsForAllProducers(moviesGroupedByProducer);
//
//        List<ProducerIntervalDTO> minIntervals = findMinIntervals(allIntervals);
//        List<ProducerIntervalDTO> maxIntervals = findMaxIntervals(allIntervals);
//
//        return new AwardIntervalDTO(minIntervals, maxIntervals);
//    }

    public AwardIntervalResponse getAwardIntervals() {
        List<MovieEntity> winningMovies = fetchWinningMovies();
        Map<String, List<MovieEntity>> moviesGroupedByProducer = groupMoviesByProducer(winningMovies);
        List<ProducerIntervalDTO> allIntervals = calculateIntervalsForAllProducers(moviesGroupedByProducer);
        List<ProducerIntervalDTO> minIntervals = findMinIntervals(allIntervals);
        List<ProducerIntervalDTO> maxIntervals = findMaxIntervals(allIntervals);
        return new AwardIntervalResponse(minIntervals, maxIntervals);
    }

    private List<MovieEntity> fetchWinningMovies() {
        return movieRepository.findByWinnerTrueOrderByYearAsc();
    }

    private Map<String, List<MovieEntity>> groupMoviesByProducer(List<MovieEntity> movies) {
        Map<String, List<MovieEntity>> moviesByProducer = new HashMap<>();
        for (MovieEntity movie : movies) {
            String[] producers = movie.getProducers().split(",");
            for (String producer : producers) {
                producer = producer.trim();
                moviesByProducer.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie);
            }
        }
        return moviesByProducer;
    }

    private List<ProducerIntervalDTO> calculateIntervalsForAllProducers(Map<String, List<MovieEntity>> moviesGroupedByProducer) {
        List<ProducerIntervalDTO> allIntervals = new ArrayList<>();
        for (Map.Entry<String, List<MovieEntity>> entry : moviesGroupedByProducer.entrySet()) {
            List<MovieEntity> producerMovies = entry.getValue();
            producerMovies.sort(Comparator.comparingInt(MovieEntity::getYear));
            for (int i = 1; i < producerMovies.size(); i++) {
                int prevYear = producerMovies.get(i - 1).getYear();
                int nextYear = producerMovies.get(i).getYear();
                int interval = nextYear - prevYear;
                allIntervals.add(new ProducerIntervalDTO(entry.getKey(), interval, prevYear, nextYear));
            }
        }
        return allIntervals;
    }

//    private List<ProducerIntervalDTO> findMinIntervals(List<ProducerIntervalDTO> intervals) {
//        ProducerIntervalDTO minInterval = intervals.stream()
//                .min(Comparator.comparingInt(ProducerIntervalDTO::getInterval))
//                .orElse(null);
//
//        if (minInterval != null && minInterval.getInterval() == 1) {
//            return Collections.singletonList(minInterval);
//        }
//        return Collections.emptyList();
//    }
//
//    private List<ProducerIntervalDTO> findMaxIntervals(List<ProducerIntervalDTO> intervals) {
//        ProducerIntervalDTO maxInterval = intervals.stream()
//                .max(Comparator.comparingInt(ProducerIntervalDTO::getInterval))
//                .orElse(null);
//
//        if (maxInterval != null && maxInterval.getInterval() == 13) {
//            return Collections.singletonList(maxInterval);
//        }
//        return Collections.emptyList();
//    }

    private List<ProducerIntervalDTO> findMinIntervals(List<ProducerIntervalDTO> intervals) {
        int minIntervalValue = intervals.stream()
                .min(Comparator.comparingInt(ProducerIntervalDTO::getInterval))
                .map(ProducerIntervalDTO::getInterval)
                .orElse(Integer.MAX_VALUE);

        return intervals.stream()
                .filter(interval -> interval.getInterval() == minIntervalValue)
                .collect(Collectors.toList());
    }

    private List<ProducerIntervalDTO> findMaxIntervals(List<ProducerIntervalDTO> intervals) {
        int maxIntervalValue = intervals.stream()
                .max(Comparator.comparingInt(ProducerIntervalDTO::getInterval))
                .map(ProducerIntervalDTO::getInterval)
                .orElse(Integer.MIN_VALUE);

        return intervals.stream()
                .filter(interval -> interval.getInterval() == maxIntervalValue)
                .collect(Collectors.toList());
    }


    public boolean loadMoviesFromCSV() {
        if (isDatabaseEmpty()) {
            List<MovieEntity> movies = readMoviesFromCSV();
            movies.forEach(movieRepository::save);
            return true;
        }
        return false;
    }



    private boolean isDatabaseEmpty() {
        return movieRepository.count() == 0;
    }

    private List<MovieEntity> readMoviesFromCSV() {
        List<MovieEntity> movies = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("movies.csv");
        try (InputStream input = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            // Skip the header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                MovieEntity movie = parseCSVLine(line);
                movies.add(movie);
            }
        } catch (IOException e) {
            throw new CsvLoadException("Failed to load movies from CSV", e);
        }
        return movies;
    }

    public MovieEntity parseCSVLine(String line) {
        String[] columns = line.split(";");
        MovieEntity movie = new MovieEntity();
        movie.setYear(Integer.parseInt(columns[0]));
        movie.setTitle(columns[1]);
        movie.setProducers(columns[2]);
        movie.setWinner("yes".equalsIgnoreCase(columns[3]));
        return movie;
    }
}
