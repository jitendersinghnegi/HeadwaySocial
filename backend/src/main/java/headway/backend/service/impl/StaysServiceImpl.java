package headway.backend.service.impl;

import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.entity.stays.BookingSource;
import headway.backend.exceptions.ResourceNotFoundException;
import headway.backend.repo.BookingSourceRepository;
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
    BookingSourceRepository bookingSourceRepo;
    @Autowired
    AuditService auditService;
    @Autowired
    private BookingSourceRepository bookingSourceRepository;

    @Override
    public List<BookingSource> getAllBookingSources() {
        return bookingSourceRepo.findAll();
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

       Optional<BookingSource> optionalBookingSource = bookingSourceRepo.findById(bookingSourceId);
       if(optionalBookingSource.isPresent()){
           BookingSource existingBookingSource = optionalBookingSource.get();
           existingBookingSource.setSourcename(bookingSource.getSourcename());
           existingBookingSource.setBankaccountno(bookingSource.getBankaccountno());
           existingBookingSource.setCommision(bookingSource.getCommision());
           BookingSource updatedBookingSource = bookingSourceRepo.save(existingBookingSource);
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
}
