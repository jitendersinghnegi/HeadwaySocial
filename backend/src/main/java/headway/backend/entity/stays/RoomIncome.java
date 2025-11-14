package headway.backend.entity.stays;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "room_income")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomIncome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="timestamp")
    private LocalDateTime timestamp;
    @Column(name="arrival_date")
    private Date arrival_date;
    @Column(name="departure_date")
    private Date departure_date;
    @Column(name="room_no")
    private int room_no;
    @Column(name="guest_name")
    private String guest_name;
    @Column(name="pax")
    private String pax;
    @Column(name="cash")
    private boolean cash;
    @Column(name="upi")
    private boolean upi;
    @Column(name="debit_card")
    private boolean debit_card;
    @Column(name="credit_card")
    private boolean credit_card;
    @Column(name="booking_source")
    private String booking_source;
    @Column(name="hotel_name")
    private String hotel_name;
    @Column(name="payment_status")
    private String payment_status;





}
