package headway.backend.entity.kitchen;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "kitchen_sale_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenSaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;
    private String itemName;

    private Integer quantity;

    private Double unitPrice;
    private Double taxRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private KitchenSale sale;
}
