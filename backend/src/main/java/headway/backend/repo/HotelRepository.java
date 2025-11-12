package headway.backend.repo;

import headway.backend.entity.stays.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
