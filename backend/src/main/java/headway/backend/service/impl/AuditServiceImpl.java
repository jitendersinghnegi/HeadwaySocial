package headway.backend.service.impl;

import headway.backend.entity.audit.AuditLog;
import headway.backend.repo.AuditLogRepository;
import headway.backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    /**
     * @param entityName
     * @param recordId
     * @param action
     * @param details
     */
    private final AuditLogRepository auditRepo;
    @Override
    public void recordAction(String entityName, String recordId, String action, String details) {
        String username = "SYSTEM";
        try{
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                username = auth.getName();
            }
        }catch(Exception e){

        }
        AuditLog log = AuditLog.builder()
                .username(username)
                .entityName(entityName)
                .recordId(recordId)
                .action(action)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        auditRepo.save(log);
    }
}
