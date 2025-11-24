package headway.backend.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class KitchenSaleResponse {

    private Long id;
    private String billNumber;
    private LocalDateTime createdAt;
    private String hotelName;
    private Double subtotal;
    private Double totalTax;
    private Double grandTotal;
    private List<SaleItemResponse> items;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaleItemResponse {
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Double unitPrice;
        private Double taxRate;
        private Double lineTotal;
    }
}