package com.example.presentation;

import com.example.application.audit.AuditService;
import com.example.domain.audit.model.AuditLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    
    private final AuditService auditService;
    
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }
    
    @GetMapping("/submissions/{submissionId}")
    public List<AuditLog> getSubmissionAuditLogs(@PathVariable String submissionId) {
        return auditService.getSubmissionAuditLogs(submissionId);
    }
    
    @GetMapping("/submissions")
    public List<AuditLog> getAllSubmissionAuditLogs() {
        return auditService.getAllSubmissionAuditLogs();
    }
} 