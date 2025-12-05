package headway.backend.service;

import headway.backend.dto.ContactDTO;
import jakarta.mail.MessagingException;

public interface ContactService {
    public void processContact(ContactDTO request) throws MessagingException;
}
