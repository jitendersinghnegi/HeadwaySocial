package headway.backend.repo;

import headway.backend.entity.kitchen.KitchenSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenSaleItemRepository extends JpaRepository<KitchenSaleItem, Long> {
}
