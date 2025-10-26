package headway.backend.controller;

import headway.backend.dto.ContactDTO;
import headway.backend.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/webshop/contact")
public class ContactController {
    private final ContactService contactService;
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    @PostMapping
    public ResponseEntity<?> receiveContact(@RequestBody ContactDTO request) {
        try {
            contactService.processContact(request);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
