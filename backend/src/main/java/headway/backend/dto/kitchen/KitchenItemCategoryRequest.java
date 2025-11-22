package headway.backend.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KitchenItemCategoryRequest {
    private String name;
    private String description;
    private Boolean isActive = true;
}
