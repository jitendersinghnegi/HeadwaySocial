package headway.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CashInHandResponse {
    private double totalCashInHand;
    private double cashIncomeKitchen;
    private double cashIncomeRooms;
    private double cashExpense;
    private List<CashPerHotel> cashPerHotel;
}
