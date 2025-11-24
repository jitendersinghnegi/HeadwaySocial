package headway.backend.service.impl;

import headway.backend.dto.dashboard.DailyRevenueDTO;
import headway.backend.dto.dashboard.HotelRevenueDTO;
import headway.backend.dto.dashboard.KitchenDashboardResponse;
import headway.backend.dto.dashboard.TopItemDTO;
import headway.backend.repo.KitchenSaleItemRepository;
import headway.backend.repo.KitchenSaleRepository;
import headway.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    /**
     * @return
     */
    @Autowired
    private final KitchenSaleRepository saleRepo;
    @Autowired
    private final KitchenSaleItemRepository itemRepo;
    @Override
    public KitchenDashboardResponse getDashboardStats() { double totalRevenue = Optional.ofNullable(saleRepo.sumGrandTotal()).orElse(0.0);
        double todayRevenue = Optional.ofNullable(saleRepo.sumGrandTotalFor(LocalDate.now())).orElse(0.0);
        long totalBills = saleRepo.count();
        double averageBill = totalBills > 0 ?
                totalRevenue / totalBills : 0.0;

        LocalDateTime from = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime to = LocalDate.now().atTime(23, 59, 59);

        List<DailyRevenueDTO> daily = saleRepo.sumByDay(from, to)
                .stream()
                .map(r -> new DailyRevenueDTO(
                        ((java.sql.Date) r[0]).toLocalDate(),
                        ((Number) r[1]).doubleValue()
                ))
                .toList();
        List<HotelRevenueDTO> byHotel = saleRepo.sumByHotel();
        List<TopItemDTO> topItems = itemRepo.findTopItems().stream()
                .limit(5)
                .toList();

        return new KitchenDashboardResponse(
                totalRevenue,
                todayRevenue,
                totalBills,
                averageBill,
                daily,
                byHotel,
                topItems
        );
    }
}
