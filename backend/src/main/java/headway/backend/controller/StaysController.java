package headway.backend.controller;

import headway.backend.dto.CategoryDTO;
import headway.backend.dto.UserResponseDTO;
import headway.backend.dto.stays.BookingSourceRequest;
import headway.backend.dto.stays.BookingSourceResponse;
import headway.backend.dto.stays.HotelRequest;
import headway.backend.dto.stays.RoomIncomeRequest;
import headway.backend.entity.category.Category;
import headway.backend.entity.stays.BookingSource;
import headway.backend.entity.stays.Hotel;
import headway.backend.entity.stays.RoomIncome;
import headway.backend.service.StaysService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("api/v1/stays")
public class StaysController {
    private StaysService staysService;

    @GetMapping("/booking-sources")
    public ResponseEntity<List<BookingSource>> getAllBookingSources(){
        List<BookingSource> bookingSources = staysService.getAllBookingSources();
        return ResponseEntity.ok(bookingSources);
    }
    @PostMapping("/booking-sources/create")
    public ResponseEntity<BookingSource> createNewBookingSource(@RequestBody BookingSourceRequest bookingServiceRequest){
        BookingSource savedBookingSource = staysService.createNew(bookingServiceRequest);
        return ResponseEntity.ok(savedBookingSource);
    }
    @PutMapping("/booking-sources/{bookingSourceId}")
    public ResponseEntity<BookingSource> updateBookingSource(@Valid @RequestBody BookingSourceRequest bookingServiceRequest , @PathVariable Long bookingSourceId){
        return ResponseEntity.ok(staysService.updateBookingSource(bookingServiceRequest,bookingSourceId));

    }
    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = staysService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    @PostMapping("/hotels/create")
    public ResponseEntity<Hotel> createNewHotel(@Valid @RequestBody HotelRequest request){
        Hotel hotel = staysService.createNewHotel(request);
        return ResponseEntity.ok(hotel);
    }

    @PutMapping("/hotels/update/{hotelId}")
    public ResponseEntity<Hotel> updateHotelDetails(@Valid @RequestBody HotelRequest request, @PathVariable Long hotelId){
        return ResponseEntity.ok(staysService.updateHotelDetails(request,hotelId));
        }
    @GetMapping("/room-sales")
    public ResponseEntity<List<RoomIncome>>  getAll(){
        List<RoomIncome> roomIncomeList = staysService.getAllRoomIncomeData();
        return ResponseEntity.ok(roomIncomeList);

    }
    @PostMapping("/room-sales/create")
    public ResponseEntity<RoomIncome> createNewIncomeEntry(@Valid @RequestBody RoomIncomeRequest request){
        RoomIncome roomIncome = staysService.createNewIncomeEntry(request);
        return ResponseEntity.ok(roomIncome);
    }


}
