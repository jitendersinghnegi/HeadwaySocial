package headway.backend.repo;

import headway.backend.dto.dashboard.TopItemDTO;
import headway.backend.entity.kitchen.KitchenSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface KitchenSaleItemRepository extends JpaRepository<KitchenSaleItem, Long> {
    @Query("""
            SELECT new headway.backend.dto.dashboard.TopItemDTO(
                i.itemName,
                COUNT(i)
            )
            FROM KitchenSaleItem i
            GROUP BY i.itemName
            ORDER BY COUNT(i) DESC
            """)
    List<TopItemDTO> findTopItems();
}
