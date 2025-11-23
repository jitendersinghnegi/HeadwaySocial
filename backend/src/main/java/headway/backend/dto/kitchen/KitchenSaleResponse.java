package headway.backend.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class KitchenSaleResponse {

    private Long id;
    private String billNumber;
    private LocalDateTime createdAt;
    private String hotelName;
}