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

@EnableJpaRepositories
@Repository
public interface RoomIncomeRepository extends JpaRepository<RoomIncome,Long> {
    @Query("""
    SELECT a FROM RoomIncome a
    WHERE (:hotel_name IS NULL OR :hotel_name = '' OR a.hotel_name = :hotel_name)
    AND (
        COALESCE(:startDate, :endDate) IS NULL
        OR a.timestamp BETWEEN COALESCE(:startDate, a.timestamp)
                           AND COALESCE(:endDate, a.timestamp)
    )
    ORDER BY a.timestamp DESC
""")
    Page<RoomIncome> findByFilters(
            @Param("hotel_name") String hotel_name,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
