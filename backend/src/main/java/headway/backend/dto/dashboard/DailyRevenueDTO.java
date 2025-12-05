package headway.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyRevenueDTO {
    private LocalDate date;
    private Double total;
}
