package headway.backend.service;

import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;

import java.util.List;

public interface WhatsAppService {
    public void sendKitchenBill(KitchenSale sale, List<KitchenSaleItem> items);
}
