package headway.backend.dto.expense;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategoryDTO {
    private Long id;
    private String name;
    private List<SupplierDTO> suppliers;
}
