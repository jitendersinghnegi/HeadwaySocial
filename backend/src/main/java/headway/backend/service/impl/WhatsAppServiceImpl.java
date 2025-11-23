package headway.backend.service.impl;

import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;
import headway.backend.service.WhatsAppService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {
    /**
     * @param sale
     * @param items
     */
    @Override
    public void sendKitchenBill(KitchenSale sale, List<KitchenSaleItem> items) {

        String phone = sale.getCustomerPhone();
        if (phone == null) return;

        StringBuilder msg = new StringBuilder();
        msg.append("Kitchen Bill #").append(sale.getBillNumber()).append("\n");
        msg.append("Customer: ").append(sale.getCustomerName()).append("\n\n");

        for (KitchenSaleItem item : items) {
            msg.append(item.getItemName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" = ")
                    .append(item.getUnitPrice() * item.getQuantity())
                    .append("\n");
        }

        msg.append("\nTotal: ").append(sale.getGrandTotal());

        // OPEN WHATSAPP WEB (Alternative)
        System.out.println("WhatsApp message: \n" + msg);

        // If API integration needed:
        // call WhatsApp Cloud API here
    }
}
