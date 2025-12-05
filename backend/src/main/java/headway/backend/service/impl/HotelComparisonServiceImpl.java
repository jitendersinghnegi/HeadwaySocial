package headway.backend.service.impl;

import headway.backend.dto.dashboard.HotelMonthValue;
import headway.backend.entity.expense.Expense;
import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.stays.RoomIncome;
import headway.backend.repo.ExpenseRepository;
import headway.backend.repo.KitchenSaleRepository;
import headway.backend.repo.RoomIncomeRepository;
import headway.backend.service.HotelComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelComparisonServiceImpl implements HotelComparisonService {
   @Autowired
   private final RoomIncomeRepository roomIncomeRepository;
   @Autowired
   private final KitchenSaleRepository kitchenSaleRepository;
   @Autowired
   private final ExpenseRepository expenseRepository;
    /**
     * @param year
     * @param metric
     * @return
     */
    @Override
    public List<HotelMonthValue> getComparison(int year, String metric) {
        String normalizedMetric = (metric == null ? "profit" : metric.toLowerCase());

        List<RoomIncome> roomIncomes = roomIncomeRepository.findByYear(year);
        List<KitchenSale> kitchenSales = kitchenSaleRepository.findByYear(year);
        List<Expense> expenses = expenseRepository.findByYear(year);

        // Map<hotelName, Map<month, value>>
        Map<String, Map<Integer, Double>> store = new LinkedHashMap<>();

        // Collect all hotel names from all three sources
        Set<String> allHotels = new LinkedHashSet<>();

        for (RoomIncome r : roomIncomes) {
            if (r.getHotel() != null && r.getHotel().getName() != null) {
                allHotels.add(r.getHotel().getName());
            }
        }

        for (KitchenSale k : kitchenSales) {
            if (k.getHotelName() != null) {
                allHotels.add(k.getHotelName());
            }
        }

        for (Expense e : expenses) {
            if (e.getHotel() != null && e.getHotel().getName() != null) {
                allHotels.add(e.getHotel().getName());
            }
        }

        // Initialize all months with 0.0 for each hotel
        for (String hotel : allHotels) {
            Map<Integer, Double> monthMap = new HashMap<>();
            for (int m = 1; m <= 12; m++) {
                monthMap.put(m, 0.0);
            }
            store.put(hotel, monthMap);
        }

        // ----- RoomIncome: revenue & commission -----
        for (RoomIncome r : roomIncomes) {
            if (r.getHotel() == null || r.getHotel().getName() == null || r.getTimestamp() == null) continue;

            String hotelName = r.getHotel().getName();
            int month = r.getTimestamp().getMonthValue();

            double revenue = r.getRevenue() != null ? r.getRevenue().doubleValue() : 0.0;
            double commission = r.getCommission() != null ? r.getCommission().doubleValue() : 0.0;

            double delta = 0.0;
            switch (normalizedMetric) {
                case "revenue":
                    delta = revenue;
                    break;
                case "expenses":
                    delta = commission;  // treat commission as expense
                    break;
                case "profit":
                default:
                    delta = revenue - commission;
                    break;
            }

            store.get(hotelName).put(
                    month,
                    store.get(hotelName).get(month) + delta
            );
        }

        // ----- KitchenSale: only revenue (no expenses tracked yet) -----
        for (KitchenSale k : kitchenSales) {
            if (k.getHotelName() == null || k.getCreatedAt() == null) continue;

            String hotelName = k.getHotelName();
            int month = k.getCreatedAt().getMonthValue();

            double revenue = k.getGrandTotal() != null ? k.getGrandTotal() : 0.0;

            double delta = 0.0;
            switch (normalizedMetric) {
                case "revenue":
                    delta = revenue;
                    break;
                case "profit":
                    delta = revenue;  // contributes positively to profit
                    break;
                case "expenses":
                default:
                    delta = 0.0;     // no kitchen expense in this model
                    break;
            }

            // If hotel did not appear in RoomIncome or Expense but appears here:
            store.computeIfAbsent(hotelName, h -> {
                Map<Integer, Double> m = new HashMap<>();
                for (int i = 1; i <= 12; i++) m.put(i, 0.0);
                return m;
            });

            store.get(hotelName).put(
                    month,
                    store.get(hotelName).get(month) + delta
            );
        }

        // ----- Expense: pure expenses -----
        for (Expense e : expenses) {
            if (e.getHotel() == null || e.getHotel().getName() == null || e.getDate() == null) continue;

            String hotelName = e.getHotel().getName();
            int month = e.getDate().getMonthValue();
            double amount = e.getAmount() != null ? e.getAmount().doubleValue() : 0.0;

            double delta = 0.0;
            switch (normalizedMetric) {
                case "expenses":
                    delta = amount;
                    break;
                case "profit":
                    delta = -amount;   // subtract from profit
                    break;
                case "revenue":
                default:
                    delta = 0.0;
                    break;
            }

            store.computeIfAbsent(hotelName, h -> {
                Map<Integer, Double> m = new HashMap<>();
                for (int i = 1; i <= 12; i++) m.put(i, 0.0);
                return m;
            });

            store.get(hotelName).put(
                    month,
                    store.get(hotelName).get(month) + delta
            );
        }

        // ----- Flatten to List<HotelMonthValue> -----
        List<HotelMonthValue> result = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, Double>> entry : store.entrySet()) {
            String hotelName = entry.getKey();
            Map<Integer, Double> months = entry.getValue();

            for (int m = 1; m <= 12; m++) {
                double val = months.getOrDefault(m, 0.0);
                result.add(new HotelMonthValue(hotelName, m, val));
            }
        }

        return result;
    }

}
