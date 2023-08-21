package indicatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerIntervalDTO {

    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}
