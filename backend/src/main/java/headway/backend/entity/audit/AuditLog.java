package headway.backend.entity.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String entityName;
    private String action;        // CREATE, UPDATE, DELETE
    private String recordId;      // e.g. entity id as string
    private LocalDateTime timestamp;

    @Column(length = 2000)
    private String details;
}
