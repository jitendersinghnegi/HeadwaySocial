package headway.backend.service;

public interface AuditService {
    public void recordAction(String entityName, String recordId, String action, String details);
}
