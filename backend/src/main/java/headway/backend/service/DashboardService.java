package headway.backend.service;

import headway.backend.dto.dashboard.DashboardKitchenSummary;
import headway.backend.dto.dashboard.DashboardRoomSummary;
import headway.backend.dto.dashboard.ExpenseSummary;
import headway.backend.dto.dashboard.KitchenDashboardResponse;

public interface DashboardService {
    public KitchenDashboardResponse getDashboardStats();
    public DashboardRoomSummary getRoomSummary(int year, Long hotelId);
    public DashboardKitchenSummary getKitchenSummary(int year, Long hotelId);
    public ExpenseSummary getExpenseSummary(int year,Long hotelId);
}
