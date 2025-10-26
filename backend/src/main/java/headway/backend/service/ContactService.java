package headway.backend.service;

import headway.backend.dto.ContactDTO;

public interface ContactService {
    public void processContact(ContactDTO request);
}
