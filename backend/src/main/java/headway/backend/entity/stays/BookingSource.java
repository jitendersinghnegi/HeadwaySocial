package headway.backend.entity.stays;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking-sources")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Long sourceid;

    @Column(unique = true,nullable = false,name="sourcename")
    private String sourcename;

    @Column(unique = true,nullable = false,name="commision")
    private Long commision;

    @Column(unique = true,nullable = true,name="bankaccountno")
    private String bankaccountno;

}
