package indicatorservice.dto;

import indicatorservice.entity.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardIntervalDTO {

    private short minIntervals;
    private short maxIntervals;

    private List<ProducerIntervalDTO> min;
    private List<ProducerIntervalDTO> max;


    public AwardIntervalDTO(List<ProducerIntervalDTO> min, List<ProducerIntervalDTO> max) {
        this.min = min;
        this.max = max;
        if (!min.isEmpty()) {
            this.minIntervals = (short) min.get(0).getInterval();
        }
        if (!max.isEmpty()) {
            this.maxIntervals = (short) max.get(0).getInterval();
        }
    }
}

