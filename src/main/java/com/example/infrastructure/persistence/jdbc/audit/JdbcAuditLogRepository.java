package com.example.infrastructure.persistence.jdbc.audit;

import com.example.domain.audit.model.AuditLog;
import com.example.domain.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("jdbcAuditLogRepository")
public class JdbcAuditLogRepository implements AuditLogRepository {
    
    private final SpringDataAuditLogRepository springDataAuditLogRepository;
    
    public JdbcAuditLogRepository(SpringDataAuditLogRepository springDataAuditLogRepository) {
        this.springDataAuditLogRepository = springDataAuditLogRepository;
    }
    
    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity entity = toEntity(auditLog);
        AuditLogEntity savedEntity = springDataAuditLogRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId) {
        return springDataAuditLogRepository.findByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AuditLog> findByEntityType(String entityType) {
        return springDataAuditLogRepository.findByEntityType(entityType)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    private AuditLog toDomain(AuditLogEntity entity) {
        return new AuditLog(
            entity.getId(),
            entity.getEntityType(),
            entity.getEntityId(),
            entity.getAction(),
            entity.getUserId(),
            entity.getOldValue(),
            entity.getNewValue(),
            entity.getTimestamp(),
            entity.getDetails()
        );
    }
    
    private AuditLogEntity toEntity(AuditLog auditLog) {
        return AuditLogEntity.builder()
            .id(auditLog.id())
            .entityType(auditLog.entityType())
            .entityId(auditLog.entityId())
            .action(auditLog.action())
            .userId(auditLog.userId())
            .oldValue(auditLog.oldValue())
            .newValue(auditLog.newValue())
            .timestamp(auditLog.timestamp())
            .details(auditLog.details())
            .build();
    }
} 