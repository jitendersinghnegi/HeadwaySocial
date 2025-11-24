package headway.backend.repo;

import headway.backend.entity.kitchen.KitchenSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KitchenSaleRepository extends JpaRepository<KitchenSale, Long> {
    List<KitchenSale> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<KitchenSale> findByHotelIdAndCreatedAtBetween(Long hotelId, LocalDateTime from, LocalDateTime to);
}
