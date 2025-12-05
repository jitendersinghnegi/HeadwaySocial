package headway.backend.repo;

import headway.backend.entity.kitchen.KitchenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitchenItemRepository extends JpaRepository<KitchenItem,Long> {
    List<KitchenItem> findByIsActiveTrue();
    List<KitchenItem> findByCategoryAndIsActiveTrue(String category);
}
