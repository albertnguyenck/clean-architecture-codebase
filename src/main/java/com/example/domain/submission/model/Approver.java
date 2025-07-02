package com.example.domain.submission.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode(of = {"userId", "order"})
@ToString
public class Approver {
    private final String userId;
    private final int order;
    private boolean approved;

    public void approve() {
        this.approved = true;
    }
} 