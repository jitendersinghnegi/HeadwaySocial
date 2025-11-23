package headway.backend.service;

import headway.backend.entity.kitchen.KitchenSale;
import headway.backend.entity.kitchen.KitchenSaleItem;

import java.util.List;

public interface EmailService {
    public void sendKitchenBillEmail(KitchenSale sale, List<KitchenSaleItem> items);
}
