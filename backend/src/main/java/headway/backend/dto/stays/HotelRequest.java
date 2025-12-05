package headway.backend.dto.stays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HotelRequest {
    private String name;
    private int rooms;
    private BigDecimal lease_amount;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
