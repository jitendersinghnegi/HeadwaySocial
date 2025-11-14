package headway.backend.repo;

import headway.backend.entity.stays.RoomIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface RoomIncomeRepository extends JpaRepository<RoomIncome,Long> {
}
