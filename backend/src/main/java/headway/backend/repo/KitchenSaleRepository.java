package headway.backend.repo;

import headway.backend.entity.kitchen.KitchenSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenSaleRepository extends JpaRepository<KitchenSale, Long> {
}
