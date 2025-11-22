package headway.backend.dto.kitchen;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KitchenItemRequest {
    private String name;
    private String category;
    private String description;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private Boolean isActive = true;



}
