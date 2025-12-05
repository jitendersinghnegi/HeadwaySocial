package headway.backend.controller;

import headway.backend.entity.audit.AuditLog;
import headway.backend.repo.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditLogRepository auditRepo;
    @GetMapping
    public Page<AuditLog> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        LocalDateTime start = (startDate != null && !startDate.isEmpty())
                ? LocalDate.parse(startDate).atStartOfDay()
                : LocalDateTime.MIN;
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
                ? LocalDate.parse(endDate).atTime(23, 59, 59)
                : LocalDateTime.now();

        return auditRepo.findByFilters(entityName, start, end, pageable);
    }
}
