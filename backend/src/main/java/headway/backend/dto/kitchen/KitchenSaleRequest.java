package headway.backend.dto.kitchen;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KitchenSaleRequest {
    private Long hotelId;

    private CustomerDTO customer;

    private List<SaleItemDTO> items;

    private Double subtotal;
    private Double totalTax;
    private Double grandTotal;

    private boolean sendEmail;
    private boolean sendWhatsApp;

    @Getter
    @Setter
    public static class CustomerDTO {
        private String type;        // WALK_IN or IN_HOUSE
        private String name;
        private String email;
        private String phone;
        private String roomNumber;
    }

    @Getter @Setter
    public static class SaleItemDTO {
        private Long itemId;
        private Integer quantity;
        private Double unitPrice;
        private Double taxRate;
    }
}
