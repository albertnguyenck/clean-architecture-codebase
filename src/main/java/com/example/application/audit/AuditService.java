package com.example.application.audit;

import com.example.domain.audit.model.AuditLog;
import com.example.domain.audit.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuditService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditService(@Qualifier("jdbcAuditLogRepository") AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public void logSubmissionChange(String submissionId, String action, String userId, 
                                   String oldValue, String newValue, String details) {
        AuditLog auditLog = new AuditLog(
            UUID.randomUUID().toString(),
            "SUBMISSION",
            submissionId,
            action,
            userId,
            oldValue,
            newValue,
            OffsetDateTime.now(),
            details
        );
        
        auditLogRepository.save(auditLog);
    }
    
    public void logSubmissionCreation(String submissionId, String userId, String details) {
        logSubmissionChange(submissionId, "CREATE", userId, null, null, details);
    }
    
    public void logSubmissionUpdate(String submissionId, String userId, 
                                   String oldValue, String newValue, String details) {
        logSubmissionChange(submissionId, "UPDATE", userId, oldValue, newValue, details);
    }
    
    public void logSubmissionApproval(String submissionId, String userId, String details) {
        logSubmissionChange(submissionId, "APPROVE", userId, null, null, details);
    }
    
    public void logSubmissionRejection(String submissionId, String userId, String reason) {
        logSubmissionChange(submissionId, "REJECT", userId, null, reason, "Submission rejected");
    }
    
    public List<AuditLog> getSubmissionAuditLogs(String submissionId) {
        return auditLogRepository.findByEntityTypeAndEntityId("SUBMISSION", submissionId);
    }
    
    public List<AuditLog> getAllSubmissionAuditLogs() {
        return auditLogRepository.findByEntityType("SUBMISSION");
    }
} 