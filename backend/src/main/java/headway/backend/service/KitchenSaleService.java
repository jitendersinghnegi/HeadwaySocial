package headway.backend.service;

import headway.backend.dto.kitchen.KitchenSaleRequest;
import headway.backend.dto.kitchen.KitchenSaleResponse;

public interface KitchenSaleService {
    public KitchenSaleResponse createSale(KitchenSaleRequest request);
    public String getItemNameFromDB(Long itemId);
}
