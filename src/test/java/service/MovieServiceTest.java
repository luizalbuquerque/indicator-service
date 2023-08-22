package service;

import indicatorservice.dto.AwardIntervalResponse;
import indicatorservice.dto.ProducerIntervalDTO;
import indicatorservice.entity.MovieEntity;
import indicatorservice.repository.MovieRepository;
import indicatorservice.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    /**
     * Método de configuração executado antes de cada teste.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Teste para verificar a correta computação dos intervalos de premiação.
     */
    @Test
    public void testGetAwardIntervals() {
        // Dados ficticios
        MovieEntity movie1 = new MovieEntity();
        movie1.setYear(2000);
        movie1.setProducers("Producer1");
        movie1.setWinner(true);

        MovieEntity movie2 = new MovieEntity();
        movie2.setYear(2001);
        movie2.setProducers("Producer1");
        movie2.setWinner(true);

        MovieEntity movie3 = new MovieEntity();
        movie3.setYear(2005);
        movie3.setProducers("Producer2");
        movie3.setWinner(true);

        MovieEntity movie4 = new MovieEntity();
        movie4.setYear(2018);
        movie4.setProducers("Producer2");
        movie4.setWinner(true);

        List<MovieEntity> mockMovies = Arrays.asList(movie1, movie2, movie3, movie4);

        // Quando o método findByWinnerTrueOrderByYearAsc for chamado, retorne a lista de filmes fictícios
        when(movieRepository.findByWinnerTrueOrderByYearAsc()).thenReturn(mockMovies);

        // Chame o método que você está testando
        AwardIntervalResponse result = movieService.getAwardIntervals();

        // Asserções para garantir que o método funciona como esperado
        assertNotNull(result);

        // Verificando os produtores com maior intervalo e mais rápido
        List<ProducerIntervalDTO> minIntervals = result.getMin();
        List<ProducerIntervalDTO> maxIntervals = result.getMax();

        // Verificando se os produtores com maior intervalo e mais rápido estão corretos
        // Substitua "Producer1" e "Producer2" pelos nomes reais dos produtores conforme sua lógica
        ProducerIntervalDTO producerWithMinInterval = minIntervals.get(0);
        assertEquals("Producer1", producerWithMinInterval.getProducer());

        ProducerIntervalDTO producerWithMaxInterval = maxIntervals.get(0);
        assertEquals("Producer2", producerWithMaxInterval.getProducer());
    }

    /**
     * Teste para carregar filmes a partir de um CSV quando o banco de dados está vazio.
     */
    @Test
    public void testLoadMoviesFromCSV() {
        when(movieRepository.count()).thenReturn(0L);
        boolean result = movieService.loadMoviesFromCSV();
        assertTrue(result);  // Espere que o resultado seja true agora
    }

    /**
     * Teste para carregar filmes a partir de um CSV quando o banco de dados já contém registros.
     */
    @Test
    public void testLoadMoviesFromCSVWithException() {
        // Supondo que o repositório retorne que o banco de dados não está vazio
        when(movieRepository.count()).thenReturn(5L);

        // Se o método foi bem-sucedido, deve retornar false
        boolean result = movieService.loadMoviesFromCSV();
        assertFalse(result);
    }

    /**
     * Teste para verificar se uma linha CSV é corretamente convertida em uma entidade de filme.
     */
    @Test
    public void testParseCSVLine() {
        // Dado um mock CSV line
        String mockCSVLine = "2000;Test Movie;Test Producer;yes";

        // Quando o método parseCSVLine é chamado
        MovieEntity result = movieService.parseCSVLine(mockCSVLine);

        // Então as asserções a seguir devem ser verdadeiras
        assertEquals(2000, result.getYear());
        assertEquals("Test Movie", result.getTitle());
        assertEquals("Test Producer", result.getProducers());
        assertTrue(result.getWinner());
    }

    /**
     * Teste para garantir que uma exceção seja lançada ao tentar analisar uma linha CSV mal formatada.
     */
    @Test
    public void testParseCSVLineWithInvalidFormat() {
        String mockCSVLine = "2000;Test Movie;yes";

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            movieService.parseCSVLine(mockCSVLine);
        });
    }

    /**
     * Teste para verificar se uma linha CSV é corretamente convertida em uma entidade de filme que não ganhou prêmio.
     */
    @Test
    public void testParseCSVLineForNonWinningMovie() {
        String mockCSVLine = "2005;Another Movie;Another Producer;no";
        MovieEntity result = movieService.parseCSVLine(mockCSVLine);
        assertFalse(result.getWinner());
    }


    @Test
    public void testLoadMoviesFromCSVIntegration() {

        // Carregue os filmes do CSV
        movieService.loadMoviesFromCSV();

        // Pega uma amostra do banco de dados
        List<MovieEntity> moviesSample = movieRepository.findAll().stream().limit(10).toList();

        // Verifique se todos os campos da amostra estão corretos
        for (MovieEntity movie : moviesSample) {
            validateMovieFields(movie);
        }
    }

    private void validateMovieFields(MovieEntity movie) {
        assertNotNull(movie.getYear(), "Year should not be null.");
        assertNotNull(movie.getTitle(), "Title should not be null.");
        assertNotNull(movie.getStudios(), "Studios should not be null.");
        assertNotNull(movie.getProducers(), "Producers should not be null.");
        assertNotNull(movie.getWinner(), "Winner should not be null.");
    }
}
