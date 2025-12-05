package headway.backend.dto.dashboard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelMonthValue {
    private String hotelName;
    private int month;
    private double value;
}
