package headway.backend.service.impl;

import headway.backend.entity.stays.BookingSource;
import headway.backend.repo.BookingSourceRepository;
import headway.backend.service.StaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StaysServiceImpl implements StaysService {
    /**
     * @return
     */
    @Autowired
    BookingSourceRepository bookingSourceRepo;
    @Override
    public List<BookingSource> getAllBookingSources() {
        return bookingSourceRepo.findAll();
    }
}
