package headway.backend.controller;

import headway.backend.dto.dashboard.KitchenDashboardResponse;
import headway.backend.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("api/v1/dashboard")
public class DashboardController {
    @Autowired
    private final DashboardService dashboardService;
    @GetMapping("/kitchen")
    public KitchenDashboardResponse getDashboard() {
        return dashboardService.getDashboardStats();
    }
}
