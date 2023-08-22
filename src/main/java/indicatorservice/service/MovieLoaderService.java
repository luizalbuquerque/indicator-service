package indicatorservice.service;

import indicatorservice.entity.MovieEntity;
import indicatorservice.exception.DataLoadException;
import indicatorservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class MovieLoaderService {

    @Autowired
    private MovieRepository movieRepository;

    @PostConstruct
    public void loadCsv() {
        if (movieRepository.count() == 0) {
            loadMoviesFromCsv();
        }
    }

    private void loadMoviesFromCsv() {
        ClassPathResource resource = new ClassPathResource("movies.csv");
        try (InputStream input = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            // Skip the header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                MovieEntity movie = parseCsvLineToMovie(line);
                movieRepository.save(movie);
            }

        } catch (IOException e) {
            throw new DataLoadException("Failed to load movies from CSV", e);
        }
    }

    private MovieEntity parseCsvLineToMovie(String line) {
        String[] columns = line.split(";");

        MovieEntity movie = new MovieEntity();
        movie.setYear(Integer.parseInt(columns[0]));
        movie.setTitle(columns[1]);
        movie.setStudios(columns[2]);
        movie.setProducers(columns[3]);
        movie.setWinner(columns.length > 4 && "yes".equalsIgnoreCase(columns[4]));

        return movie;
    }
}
