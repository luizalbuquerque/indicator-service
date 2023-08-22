package indicatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardIntervalResponse {

    private List<ProducerIntervalDTO> min;
    private List<ProducerIntervalDTO> max;

    public AwardIntervalResponse(AwardIntervalDTO awardIntervalDTO) {
        this.min = awardIntervalDTO.getMin();
        this.max = awardIntervalDTO.getMax();
    }

    public List<ProducerIntervalDTO> getMin() {
        return min;
    }

    public void setMin(List<ProducerIntervalDTO> min) {
        this.min = min;
    }

    public List<ProducerIntervalDTO> getMax() {
        return max;
    }

    public void setMax(List<ProducerIntervalDTO> max) {
        this.max = max;
    }
}

