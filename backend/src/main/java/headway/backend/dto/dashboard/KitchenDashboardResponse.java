package headway.backend.dto.dashboard;

import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitchenDashboardResponse {private double totalRevenue;
    private double todayRevenue;
    private long totalBills;
    private double averageBill;

    private List<DailyRevenueDTO> dailyChart;
    private List<HotelRevenueDTO> hotelRevenue;
    private List<TopItemDTO> topItems;

}
