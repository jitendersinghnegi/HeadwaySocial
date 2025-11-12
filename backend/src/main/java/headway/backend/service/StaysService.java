package headway.backend.service;

import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.entity.stays.BookingSource;

import java.util.List;

public interface StaysService {
    List<BookingSource> getAllBookingSources();
    BookingSource createNew(BookingSourceRequest bookingSource);
    BookingSource updateBookingSource(BookingSourceRequest bookingSource,Long bookingSourceId);
}
