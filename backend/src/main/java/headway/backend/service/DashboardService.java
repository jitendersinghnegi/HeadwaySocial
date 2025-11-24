package headway.backend.service;

import headway.backend.dto.dashboard.KitchenDashboardResponse;

public interface DashboardService {
    public KitchenDashboardResponse getDashboardStats();
}
