package headway.backend.service.impl;

import headway.backend.dto.ContactDTO;
import headway.backend.entity.contact.Contact;
import headway.backend.repo.ContactRepository;
import headway.backend.service.ContactService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class ContactServiceImpl implements ContactService {
    /**
     * @param request
     */
    @Autowired
    private ContactRepository contactRepository;
    private  JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${contact.notify.to}")
    private String notifyTo;

    public ContactServiceImpl(ContactRepository contactRepository, JavaMailSender mailSender,SpringTemplateEngine templateEngine) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    @Override
    public void processContact(ContactDTO request) throws MessagingException {
        // Save to DB
        Contact entity = new Contact();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setMessage(request.getMessage());
        contactRepository.save(entity);
        //Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("name", request.getName());
        context.setVariable("email", request.getEmail());
        context.setVariable("message", request.getMessage());
        //Render the HTML template
        String htmlContent = templateEngine.process("email-template", context);
        //Create MIME email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(htmlContent, true); // true = HTML
        helper.setTo(notifyTo);
        helper.setSubject("New enquiry from " + request.getName());
        helper.setFrom(mailFrom);
        // Send email
        mailSender.send(mimeMessage);
    }


}
