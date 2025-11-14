package headway.backend.service.impl;

import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.dto.stays.HotelRequest;
import headway.backend.entity.stays.BookingSource;
import headway.backend.entity.stays.Hotel;
import headway.backend.exceptions.ResourceNotFoundException;
import headway.backend.repo.BookingSourceRepository;
import headway.backend.repo.HotelRepository;
import headway.backend.service.AuditService;
import headway.backend.service.StaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaysServiceImpl implements StaysService {
    /**
     * @return
     */
    @Autowired
    AuditService auditService;
    @Autowired
    BookingSourceRepository bookingSourceRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Override
    public List<BookingSource> getAllBookingSources() {
        return bookingSourceRepository.findAll();
    }

    /**
     * @param bookingSource
     * @return
     */
    @Override
    public BookingSource createNew(BookingSourceRequest bookingSource) {
        BookingSource newBookingSource = new BookingSource();
        newBookingSource.setSourcename(bookingSource.getSourcename());
        newBookingSource.setBankaccountno(bookingSource.getBankaccountno());
        newBookingSource.setCommision(bookingSource.getCommision());
        BookingSource createdBookingSource = bookingSourceRepository.save(newBookingSource);
        auditService.recordAction(
                "BookingSource",
                createdBookingSource.getSourceid().toString(),
                "Create",
                "Created Booking Source : "+ createdBookingSource.getSourcename()
        );
        return createdBookingSource;

    }

    /**
     * @param bookingSource
     * @param bookingSourceId
     * @return
     */
    @Override
    public BookingSource updateBookingSource(BookingSourceRequest bookingSource, Long bookingSourceId) {

       Optional<BookingSource> optionalBookingSource = bookingSourceRepository.findById(bookingSourceId);
       if(optionalBookingSource.isPresent()){
           BookingSource existingBookingSource = optionalBookingSource.get();
           existingBookingSource.setSourcename(bookingSource.getSourcename());
           existingBookingSource.setBankaccountno(bookingSource.getBankaccountno());
           existingBookingSource.setCommision(bookingSource.getCommision());
           BookingSource updatedBookingSource = bookingSourceRepository.save(existingBookingSource);
           auditService.recordAction(
                   "BookingSource",
                   updatedBookingSource.getSourceid().toString(),
                   "Update",
                   "updatedBookingSource Booking Source : "+ updatedBookingSource.getSourcename()
           );
           return updatedBookingSource;
       }else{
           throw new ResourceNotFoundException("BookingSource","BookingSourceID",bookingSourceId);
       }
    }

    /**
     * @return
     */
    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    /**
     * @param hotelRequest
     * @return
     */
    @Override
    public Hotel createNewHotel(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setLease_amount(hotelRequest.getLease_amount());
        hotel.setRooms(hotelRequest.getRooms());
        hotel.setLongitude(hotelRequest.getLongitude());
        hotel.setLatitude(hotelRequest.getLatitude());
        Hotel createdHotel = hotelRepository.save(hotel);
        auditService.recordAction(
                "Hotel",
                createdHotel.getId().toString(),
                "Create",
                "Created new  hotel : "+ createdHotel.getName()
        );
        return createdHotel;
    }

    /**
     * @param request
     * @param hotelId
     * @return
     */
    @Override
    public Hotel updateHotelDetails(HotelRequest request, Long hotelId) {
       Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
       if(optionalHotel.isPresent()){
           Hotel existingHotel = optionalHotel.get();
           existingHotel.setLatitude(request.getLatitude());
           existingHotel.setRooms(request.getRooms());
           existingHotel.setName(request.getName());
           existingHotel.setLongitude(request.getLongitude());
           existingHotel.setLease_amount(request.getLease_amount());
           Hotel updateHotel = hotelRepository.save(existingHotel);
           auditService.recordAction(
                   "Hotel",
                   updateHotel.getId().toString(),
                   "Updated",
                   "Updated   hotel : "+ updateHotel.getName()
           );
           return updateHotel;

       }else{
           throw new ResourceNotFoundException("Hotel","HotelID",hotelId);
       }

    }
}
