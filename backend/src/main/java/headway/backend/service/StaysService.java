package headway.backend.service;

import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.dto.stays.HotelRequest;
import headway.backend.dto.stays.RoomIncomeRequest;
import headway.backend.entity.stays.BookingSource;
import headway.backend.entity.stays.Hotel;
import headway.backend.entity.stays.RoomIncome;

import java.util.List;

public interface StaysService {
    List<BookingSource> getAllBookingSources();
    BookingSource createNew(BookingSourceRequest bookingSource);
    BookingSource updateBookingSource(BookingSourceRequest bookingSource,Long bookingSourceId);
    List<Hotel> getAllHotels();
    Hotel createNewHotel(HotelRequest property);
    Hotel updateHotelDetails(HotelRequest request, Long hotelId);
    List<RoomIncome> getAllRoomIncomeData();
    RoomIncome createNewIncomeEntry(RoomIncomeRequest request);

}
