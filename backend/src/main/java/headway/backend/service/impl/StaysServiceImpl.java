package headway.backend.service.impl;

import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.dto.stays.HotelRequest;
import headway.backend.dto.stays.RoomIncomeRequest;
import headway.backend.entity.stays.BookingSource;
import headway.backend.entity.stays.Hotel;
import headway.backend.entity.stays.RoomIncome;
import headway.backend.exceptions.ResourceNotFoundException;
import headway.backend.repo.BookingSourceRepository;
import headway.backend.repo.HotelRepository;
import headway.backend.repo.RoomIncomeRepository;
import headway.backend.service.AuditService;
import headway.backend.service.StaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    RoomIncomeRepository roomIncomeRepository;

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

    /**
     * @return
     */
    @Override
    public Page<RoomIncome> getAllRoomIncomeData(int page, int size, String hotel_name, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        LocalDateTime start = (startDate != null && !startDate.isEmpty())
                ? LocalDate.parse(startDate).atStartOfDay()
                : LocalDateTime.MIN;
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
                ? LocalDate.parse(endDate).atTime(23, 59, 59)
                : LocalDateTime.now();
        System.out.println("Values passed are ---"+hotel_name+"........"+size);
        return roomIncomeRepository.findByFilters(hotel_name,start,end,pageable);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public RoomIncome createNewIncomeEntry(RoomIncomeRequest request) {
        RoomIncome roomIncome = new RoomIncome();
        roomIncome.setCash(request.isCash());
        roomIncome.setArrival_date(request.getArrival_date());
        roomIncome.setBooking_source(request.getBooking_source());
        roomIncome.setPax(request.getPax());
        roomIncome.setRoom_no(request.getRoom_no());
        roomIncome.setDebit_card(request.isDebit_card());
        roomIncome.setCredit_card(request.isCredit_card());
        roomIncome.setUpi(request.isUpi());
        roomIncome.setTimestamp(LocalDateTime.now());
        roomIncome.setDeparture_date(request.getDeparture_date());
        roomIncome.setGuest_name(request.getGuest_name());
        roomIncome.setHotel_name(request.getHotel_name());
        roomIncome.setPayment_status(request.getPayment_status());
        roomIncome.setAmount(request.getAmount());
        RoomIncome savedRoomIncome = roomIncomeRepository.save(roomIncome);
        auditService.recordAction(
                "RoomIncome",
                savedRoomIncome.getId().toString(),
                "Created",
                "Created   Room Income for Room No : "+ savedRoomIncome.getRoom_no() + "Guest Name : "+ savedRoomIncome.getGuest_name() + "for Hotel "+savedRoomIncome.getHotel_name()
        );
        return null;
    }
}
