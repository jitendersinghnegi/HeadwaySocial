package headway.backend.service.impl;

import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import headway.backend.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private final JavaMailSender mailSender;
    /**
     * @param sale
     * @param items
     */
    @Override
    public void sendKitchenBillEmail(KitchenSale sale, List<KitchenSaleItem> items) {
        String to = sale.getCustomerEmail();
        String subject = "Your Kitchen Bill #" + sale.getBillNumber();

        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Kitchen Bill</h2>");
        sb.append("<p>Bill Number: ").append(sale.getBillNumber()).append("</p>");
        sb.append("<p>Customer: ").append(sale.getCustomerName()).append("</p>");
        sb.append("<table border='1' cellpadding='6'>");
        sb.append("<tr><th>Item</th><th>Qty</th><th>Rate</th><th>Amount</th></tr>");

        for (KitchenSaleItem item : items) {
            sb.append("<tr>")
                    .append("<td>").append(item.getItemName()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>").append(item.getUnitPrice()).append("</td>")
                    .append("<td>").append(item.getUnitPrice() * item.getQuantity()).append("</td>")
                    .append("</tr>");
        }

        sb.append("</table>");
        sb.append("<p><b>Total: ").append(sale.getGrandTotal()).append("</b></p>");

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(sb.toString(), true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }

        mailSender.send(message);
    }
}
