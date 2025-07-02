package com.example.infrastructure.persistence.jdbc.audit;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataAuditLogRepository extends CrudRepository<AuditLogEntity, String> {
    List<AuditLogEntity> findByEntityTypeAndEntityId(String entityType, String entityId);
    List<AuditLogEntity> findByEntityType(String entityType);
} 