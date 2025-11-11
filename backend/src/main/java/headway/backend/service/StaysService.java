package headway.backend.service;

import headway.backend.entity.stays.BookingSource;

import java.util.List;

public interface StaysService {
    List<BookingSource> getAllBookingSources();
}
