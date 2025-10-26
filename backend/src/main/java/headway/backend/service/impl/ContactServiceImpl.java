package headway.backend.service.impl;

import headway.backend.dto.ContactDTO;
import headway.backend.entity.contact.Contact;
import headway.backend.repo.ContactRepository;
import headway.backend.service.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    /**
     * @param request
     */
    @Autowired
    private ContactRepository contactRepository;
    private  JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${contact.notify.to}")
    private String notifyTo;

    public ContactServiceImpl(ContactRepository contactRepository, JavaMailSender mailSender) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
    }


    @Override
    public void processContact(ContactDTO request) {
        // Save to DB
        Contact entity = new Contact();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setMessage(request.getMessage());
        contactRepository.save(entity);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(notifyTo);
        message.setSubject("New Contact Form Message from " + request.getName());
        message.setText(
                "Name: " + request.getName() + "\n" +
                        "Email: " + request.getEmail() + "\n\n" +
                        request.getMessage()
        );
        mailSender.send(message);
    }


}
