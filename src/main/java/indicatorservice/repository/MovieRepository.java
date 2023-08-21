package indicatorservice.repository;

import indicatorservice.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findByWinnerTrueOrderByYearAsc();

}

