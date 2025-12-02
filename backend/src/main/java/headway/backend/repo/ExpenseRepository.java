package headway.backend.repo;

import headway.backend.entity.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAll(Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE YEAR(e.date) = :year")
    List<Expense> findByYear(int year);

    @Query("SELECT e FROM Expense e WHERE YEAR(e.date) = :year AND e.hotel.id = :hotelId")
    List<Expense> findByYearAndHotel(int year, Long hotelId);
}
