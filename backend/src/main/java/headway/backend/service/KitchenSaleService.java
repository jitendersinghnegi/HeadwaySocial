package headway.backend.service;

import headway.backend.dto.kitchen.KitchenSaleRequest;
import headway.backend.dto.kitchen.KitchenSaleResponse;

import java.time.LocalDate;
import java.util.List;

public interface KitchenSaleService {
    public KitchenSaleResponse createSale(KitchenSaleRequest request);
    public String getItemNameFromDB(Long itemId);
    public List<KitchenSaleResponse> getSalesFiltered(Long hotelId, LocalDate from, LocalDate to);
}
