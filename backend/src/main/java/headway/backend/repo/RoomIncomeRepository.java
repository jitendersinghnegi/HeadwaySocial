package headway.backend.repo;

import headway.backend.entity.stays.RoomIncome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface RoomIncomeRepository extends JpaRepository<RoomIncome,Long> {
    @Query("""
SELECT a FROM RoomIncome a
WHERE (:hotelId IS NULL OR a.hotel.id = :hotelId)
AND (
    COALESCE(:startDate, :endDate) IS NULL
    OR a.timestamp BETWEEN COALESCE(:startDate, a.timestamp)
                       AND COALESCE(:endDate, a.timestamp)
)
ORDER BY a.timestamp DESC
""")
    Page<RoomIncome> findByFilters(
            @Param("hotelId") Long hotelId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
    @Query("SELECT r FROM RoomIncome r WHERE YEAR(r.timestamp) = :year")
    List<RoomIncome> findByYear(int year);

    @Query("SELECT r FROM RoomIncome r WHERE YEAR(r.timestamp) = :year AND r.hotel.id = :hotelId")
    List<RoomIncome> findByYearAndHotel(int year, Long hotelId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RoomIncome r WHERE YEAR(r.timestamp) = :year AND r.cash = true ")
    double getTotalRoomCashIncome(int year);

    @Query("SELECT COALESCE(SUM(r.amount),0) FROM RoomIncome r WHERE YEAR(r.timestamp) = :year AND r.hotel.id = :hotelId AND r.cash = true")
    double getCashIncomeRoomByHotel(int year, Long hotelId);
}
