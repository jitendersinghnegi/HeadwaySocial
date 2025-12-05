package headway.backend.dto.stays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingSourceRequest {
    private String sourcename;
    private Long commision;
    private String bankaccountno;
}
