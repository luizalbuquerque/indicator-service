package indicatorservice.controller;

import indicatorservice.entity.MovieEntity;
import indicatorservice.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class CsvLoaderController {

    @Autowired
    private MovieRepository movieRepository;

    @PostConstruct
    public void loadCsv() {
        if (movieRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("movies.csv");
            try (InputStream input = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

                String line;
                // Skip the header
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(";");

                    MovieEntity movie = new MovieEntity();
                    movie.setYear(Integer.parseInt(columns[0]));
                    movie.setTitle(columns[1]);
                    movie.setStudios(columns[2]);
                    movie.setProducers(columns[3]);
                    movie.setWinner(columns.length > 4 && "yes".equalsIgnoreCase(columns[4]));

                    movieRepository.save(movie);
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to load movies from CSV", e);
            }
        }
    }
}
