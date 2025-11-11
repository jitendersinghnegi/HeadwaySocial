package headway.backend.repo;

import headway.backend.entity.stays.BookingSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface BookingSourceRepository extends JpaRepository<BookingSource,Long> {
}
