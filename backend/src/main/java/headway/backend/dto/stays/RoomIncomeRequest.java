package headway.backend.dto.stays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomIncomeRequest {

    private LocalDateTime timestamp;
    private Date arrival_date;

    private Date departure_date;

    private int room_no;

    private String guest_name;

    private String pax;

    private boolean cash;

    private boolean upi;

    private boolean debit_card;

    private boolean credit_card;

    private String booking_source;

    private String hotel_name;

    private String payment_status;

    private BigDecimal amount;
}
