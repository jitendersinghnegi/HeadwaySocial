package headway.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopItemDTO {
    private String itemName;
    private Long count;
}
