package headway.backend.utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import headway.backend.dto.kitchen.KitchenSaleResponse;
import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.util.List;

@Component
@RequiredArgsConstructor
public class PdfGenerator {
    private final SpringTemplateEngine emailTemplateEngine;

    public byte[] generateInvoicePdf(KitchenSale sale, List<KitchenSaleItem> items) {
        List<KitchenSaleResponse.SaleItemResponse> itemDTOs = items.stream()
                .map(i -> new KitchenSaleResponse.SaleItemResponse(
                        i.getItemId(),
                        i.getItemName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getTaxRate(),
                        i.getUnitPrice() * i.getQuantity()  // lineTotal FIX
                ))
                .toList();

        Context context = new Context();
        context.setVariable("billNumber", sale.getBillNumber());
        context.setVariable("createdAt", sale.getCreatedAt());
        context.setVariable("hotelName", sale.getHotelName());
        context.setVariable("customerName", sale.getCustomerName());
        context.setVariable("customerType", sale.getCustomerType());
        context.setVariable("roomNumber", sale.getRoomNumber());
        context.setVariable("customerPhone", sale.getCustomerPhone());
        context.setVariable("items", itemDTOs);
        context.setVariable("subtotal", sale.getSubtotal());
        context.setVariable("totalTax", sale.getTotalTax());
        context.setVariable("grandTotal", sale.getGrandTotal());

        String html = emailTemplateEngine.process("kitchen-invoice", context);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, "/");
        builder.toStream(out);

        try {
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating PDF", e);
        }

        return out.toByteArray();
    }
}
