package headway.backend.repo;

import headway.backend.entity.kitchen.KitchenItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KitchenItemCategoryRepository extends JpaRepository<KitchenItemCategory,Long> {
    List<KitchenItemCategory> findByIsActiveTrue();
}
