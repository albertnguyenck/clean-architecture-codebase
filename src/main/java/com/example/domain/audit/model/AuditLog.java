package com.example.domain.audit.model;

import java.time.OffsetDateTime;

public record AuditLog(
    String id,
    String entityType,
    String entityId,
    String action,
    String userId,
    String oldValue,
    String newValue,
    OffsetDateTime timestamp,
    String details
) {} 