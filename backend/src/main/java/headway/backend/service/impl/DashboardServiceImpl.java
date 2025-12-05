package headway.backend.service.impl;

import headway.backend.dto.dashboard.*;
import headway.backend.repo.*;
import headway.backend.service.DashboardService;
import headway.backend.service.ExpenseService;
import headway.backend.service.KitchenSaleService;
import headway.backend.service.StaysService;
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
    private final RoomIncomeRepository roomIncomeRepo;
    @Autowired
    private final ExpenseRepository expenseRepo;
    @Autowired
    private final KitchenSaleItemRepository itemRepo;
    @Autowired
    private final HotelRepository hotelRepo;
    @Autowired
    private final StaysService stayService;
    @Autowired
    private final KitchenSaleService kitchenSaleService;
    @Autowired
    private final ExpenseService expenseService;
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

    /**
     * @param year
     * @param hotelId
     * @return
     */
    @Override
    public DashboardRoomSummary getRoomSummary(int year, Long hotelId) {
        return stayService.getSummary(year,hotelId);
    }

    /**
     * @param year
     * @param hotelId
     * @return
     */
    @Override
    public DashboardKitchenSummary getKitchenSummary(int year, Long hotelId) {
        return kitchenSaleService.getSummary(year,hotelId);
    }

    /**
     * @param year
     * @param hotelId
     * @return
     */
    @Override
    public ExpenseSummary getExpenseSummary(int year, Long hotelId) {
        return expenseService.getSummary(year,hotelId);
    }

    /**
     * @return
     */
    @Override
    public CashInHandResponse getCashInHand(int year) {
        double roomCash = roomIncomeRepo.getTotalRoomCashIncome(year);
        double kitchenCash = saleRepo.getTotalKitchenCashIncome(year);
        double expensesCash = expenseRepo.getTotalCashExpenses(year);
        double totalCashInHand = (roomCash + kitchenCash) - expensesCash;
        List<CashPerHotel> perHotel = hotelRepo.findAll().stream().map(h -> {
            double r = roomIncomeRepo.getCashIncomeRoomByHotel(year,h.getId());
            double k = saleRepo.getCashIncomeKitchenByHotel(year,h.getId());
            double e = expenseRepo.getCashExpensesByHotel(year,h.getId());
            return new CashPerHotel(h.getName(), (r + k) - e,r,k,e);
        }).toList();

        return new CashInHandResponse(totalCashInHand,roomCash,kitchenCash,expensesCash, perHotel);
    }

}
