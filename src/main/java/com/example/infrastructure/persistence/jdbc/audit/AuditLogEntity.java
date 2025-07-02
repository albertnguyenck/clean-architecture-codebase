package com.example.infrastructure.persistence.jdbc.audit;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Builder
@Table("audit_logs")
public class AuditLogEntity {
    @Id
    private String id;
    private String entityType;
    private String entityId;
    private String action;
    private String userId;
    private String oldValue;
    private String newValue;
    private OffsetDateTime timestamp;
    private String details;
} 