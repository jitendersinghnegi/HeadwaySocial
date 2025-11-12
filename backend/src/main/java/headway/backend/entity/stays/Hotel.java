package headway.backend.entity.stays;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true,nullable = false,name="name")
    private String name;

    @Column(nullable = false,name="rooms")
    private int rooms;

    @Column(nullable = false,name="lease_amount",precision = 15, scale = 2)
    private BigDecimal lease_amount;

    @Column(nullable = false,name="latitude",precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false,name="longitude",precision = 9, scale = 6)
    private BigDecimal longitude;




}
