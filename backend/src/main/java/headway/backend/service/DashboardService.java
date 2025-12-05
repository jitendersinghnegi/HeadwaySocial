package headway.backend.service;

import headway.backend.dto.dashboard.*;

public interface DashboardService {
    public KitchenDashboardResponse getDashboardStats();
    public DashboardRoomSummary getRoomSummary(int year, Long hotelId);
    public DashboardKitchenSummary getKitchenSummary(int year, Long hotelId);
    public ExpenseSummary getExpenseSummary(int year,Long hotelId);
    public CashInHandResponse getCashInHand(int year);
}
