package com.neonajam.neotracker.dto;

import com.neonajam.neotracker.model.ChangeType;
import com.neonajam.neotracker.model.DataChangeLog;

import java.time.Instant;

public class ChangeLogResponse {

    private Long asteroidId;
    private Instant changedAt;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private ChangeType changeType;

    public static ChangeLogResponse from(DataChangeLog log) {
        ChangeLogResponse r = new ChangeLogResponse();
        r.asteroidId = log.getAsteroid().getId();
        r.changedAt = log.getChangedAt();
        r.fieldName = log.getFieldName();
        r.oldValue = log.getOldValue();
        r.newValue = log.getNewValue();
        r.changeType = log.getChangeType();
        return r;
    }

    public Long getAsteroidId() {
        return asteroidId;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public ChangeType getChangeType() {
        return changeType;
    }
}
