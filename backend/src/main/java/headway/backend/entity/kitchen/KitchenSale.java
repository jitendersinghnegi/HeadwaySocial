package headway.backend.entity.kitchen;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kitchen_sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billNumber;

    // HOTEL INFO
    @Column(nullable = false)
    private Long hotelId;

    @Column(nullable = false)
    private String hotelName;

    private Double subtotal;
    private Double totalTax;
    private Double grandTotal;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // WALK_IN or IN_HOUSE
    private String customerType;
    private String roomNumber;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenSaleItem> items = new ArrayList<>();
}
