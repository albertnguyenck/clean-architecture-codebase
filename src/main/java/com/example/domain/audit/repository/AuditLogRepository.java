package com.example.domain.audit.repository;

import com.example.domain.audit.model.AuditLog;

import java.util.List;

public interface AuditLogRepository {
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    List<AuditLog> findByEntityType(String entityType);
} 