package headway.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CashPerHotel {
    private String hotelName;
    private double cashInHand;
    private double roomIncome;
    private double kitchenIncome;
    private double expense;
}
