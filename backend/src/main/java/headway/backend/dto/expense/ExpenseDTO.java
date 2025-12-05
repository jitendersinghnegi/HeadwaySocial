package headway.backend.dto.expense;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {
    private Long id;

    private String date;
    private String type;

    private Long hotelId;
    private String hotelName;

    private Long categoryId;
    private String categoryName;

    private Long supplierId;
    private String supplierName;

    private BigDecimal amount;

    private String paymentMethod;
    private String paymentStatus;

    private String billNo;
    private String billFileName;
    private String billFilePath;
}
