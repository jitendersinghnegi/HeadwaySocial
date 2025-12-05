package headway.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotelRevenueDTO {
    private String hotelName;
    private Double total;
}
