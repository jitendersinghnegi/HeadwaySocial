package headway.backend.service.impl;

import headway.backend.dto.kitchen.KitchenSaleResponse;
import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import headway.backend.service.EmailService;
import headway.backend.utils.PdfGenerator;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private final PdfGenerator pdfGenerator;
    private final SpringTemplateEngine emailTemplateEngine;
    @Value("${contact.notify.to}")
    private String mailFrom;
    /**
     * @param sale
     * @param items
     */
    @Override
    public void sendKitchenBillEmail(KitchenSale sale, List<KitchenSaleItem> items) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            // Prepare thymeleaf context
            Context context = new Context();
            context.setVariable("billNumber", sale.getBillNumber());
            context.setVariable("createdAt", sale.getCreatedAt());
            context.setVariable("hotelName", sale.getHotelName());
            context.setVariable("customerName", sale.getCustomerName());
            context.setVariable("customerType", sale.getCustomerType());
            context.setVariable("roomNumber", sale.getRoomNumber());
            context.setVariable("customerPhone", sale.getCustomerPhone());

            context.setVariable("items", items.stream().map(i ->
                    new KitchenSaleResponse.SaleItemResponse(
                            i.getItemId(),
                            i.getItemName(),
                            i.getQuantity(),
                            i.getUnitPrice(),
                            i.getTaxRate(),
                            i.getUnitPrice() * i.getQuantity()
                    )
            ).toList());

            context.setVariable("subtotal", sale.getSubtotal());
            context.setVariable("totalTax", sale.getTotalTax());
            context.setVariable("grandTotal", sale.getGrandTotal());

            // Process template
            String htmlContent = emailTemplateEngine.process("kitchen-invoice", context);

            helper.setTo(sale.getCustomerEmail());
            helper.setSubject("Kitchen Invoice - " + sale.getBillNumber());
            helper.setText(htmlContent, true);

            // EMBED COMPANY LOGO
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("companyLogo", logo);

            // GENERATE PDF
            byte[] pdfBytes = pdfGenerator.generateInvoicePdf(sale, items);

            helper.addAttachment("Invoice-" + sale.getBillNumber() + ".pdf",
                    new ByteArrayResource(pdfBytes));

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }

    /**
     * @param to
     * @param subject
     * @param text
     */
    @Override
    public void sendForgotPasswordEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Optional: Set FROM (required by some SMTP servers)
        message.setFrom(mailFrom);

        mailSender.send(message);
    }
}
