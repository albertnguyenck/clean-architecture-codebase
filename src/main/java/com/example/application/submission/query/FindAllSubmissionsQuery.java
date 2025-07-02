package com.example.application.submission.query;

public record FindAllSubmissionsQuery(
    int page,
    int size,
    String sortBy,
    String sortDirection
) {
    public FindAllSubmissionsQuery {
        // Default values
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100; // Max page size
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "desc";
    }
    
    public FindAllSubmissionsQuery() {
        this(0, 10, "createdAt", "desc");
    }
} 