package headway.backend.controller;

import headway.backend.dto.dashboard.DashboardKitchenSummary;
import headway.backend.dto.dashboard.DashboardRoomSummary;
import headway.backend.dto.dashboard.ExpenseSummary;
import headway.backend.dto.dashboard.KitchenDashboardResponse;
import headway.backend.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/kitchen/summary")
    public DashboardKitchenSummary getKitchenSummary(@RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year,
                                                     @RequestParam(required = false) Long hotelId){
        return dashboardService.getKitchenSummary(year,hotelId);
    }
    @GetMapping("/stays/summary")
    public DashboardRoomSummary getRoomSummary(
            @RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year,
            @RequestParam(required = false) Long hotelId
    ) {
        return dashboardService.getRoomSummary(year, hotelId);
    }

    @GetMapping("/expense/summary")
    public ExpenseSummary getExpenseSummary(
            @RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year,
            @RequestParam(required = false) Long hotelId
    ) {
        return dashboardService.getExpenseSummary(year, hotelId);
    }
}
