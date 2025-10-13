package headway.backend.repo;

import headway.backend.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
