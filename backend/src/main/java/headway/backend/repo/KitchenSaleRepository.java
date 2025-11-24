package headway.backend.repo;

import headway.backend.dto.dashboard.DailyRevenueDTO;
import headway.backend.dto.dashboard.HotelRevenueDTO;
import headway.backend.entity.kitchen.KitchenSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface KitchenSaleRepository extends JpaRepository<KitchenSale, Long> {
    @Query("SELECT SUM(s.grandTotal) FROM KitchenSale s")
    Double sumGrandTotal();

    @Query("SELECT SUM(s.grandTotal) FROM KitchenSale s WHERE DATE(s.createdAt) = :date")
    Double sumGrandTotalFor(LocalDate date);

    // DAILY REVENUE (native SQL because of DATE())
    @Query(
            value = """
                SELECT 
                    DATE(s.created_at) AS date,
                    SUM(s.grand_total) AS total
                FROM kitchen_sales s
                WHERE s.created_at BETWEEN :from AND :to
                GROUP BY DATE(s.created_at)
                ORDER BY date
                """,
            nativeQuery = true)
    List<Object[]> sumByDay(LocalDateTime from, LocalDateTime to);

    // HOTEL-WISE REVENUE (JPQL works fine)
    @Query("""
            SELECT new headway.backend.dto.dashboard.HotelRevenueDTO(
                s.hotelName,
                SUM(s.grandTotal)
            )
            FROM KitchenSale s
            GROUP BY s.hotelName
            ORDER BY SUM(s.grandTotal) DESC
            """)
    List<HotelRevenueDTO> sumByHotel();

    @Query("SELECT AVG(s.grandTotal) FROM KitchenSale s")
    Double averageGrandTotal();

    List<KitchenSale> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<KitchenSale> findByHotelIdAndCreatedAtBetween(Long hotelId, LocalDateTime from, LocalDateTime to);
}
