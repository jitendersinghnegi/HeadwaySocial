package headway.backend.controller;

import headway.backend.dto.dashboard.*;
import headway.backend.service.DashboardService;
import headway.backend.service.HotelComparisonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("api/v1/dashboard")
public class DashboardController {
    @Autowired
    private final DashboardService dashboardService;
    @Autowired
    private final HotelComparisonService hotelComparisonService;
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

    @GetMapping("/hotel-comparison")
    public List<HotelMonthValue> getHotelComparison(
            @RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year,
            @RequestParam(defaultValue = "profit") String metric
    ) {
        return hotelComparisonService.getComparison(year, metric);
    }

    @GetMapping("/cash-in-hand")
    public CashInHandResponse getCashInHand(@RequestParam(defaultValue = "#{T(java.time.Year).now().getValue()}") int year) {
        return dashboardService.getCashInHand(year);
    }
}
