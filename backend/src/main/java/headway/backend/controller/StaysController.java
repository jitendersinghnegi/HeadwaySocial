package headway.backend.controller;

import headway.backend.dto.UserResponseDTO;
import headway.backend.dto.stays.BookingSourceResponse;
import headway.backend.entity.stays.BookingSource;
import headway.backend.service.StaysService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("api/v1/stays")
public class StaysController {
    private StaysService staysService;

    @GetMapping("/booking-sources")
    public ResponseEntity<List<BookingSource>> getAllBookingSources(){
        List<BookingSource> bookingSources = staysService.getAllBookingSources();
        return ResponseEntity.ok(bookingSources);
    }
}
