package com.neonajam.neotracker.dto;

import com.neonajam.neotracker.model.Watch;

public class WatchResponse {

    private Long id;
    private Long asteroidId;
    private String asteroidName;
    private String label;
    private boolean alertIfHazardous;
    private Double alertIfMissDistanceKmUnder;
    private boolean triggered;

    public static WatchResponse from(Watch w, boolean triggered) {
        WatchResponse r = new WatchResponse();
        r.id = w.getId();
        r.asteroidId = w.getAsteroid().getId();
        r.asteroidName = w.getAsteroid().getName();
        r.label = w.getLabel();
        r.alertIfHazardous = w.isAlertIfHazardous();
        r.alertIfMissDistanceKmUnder = w.getAlertIfMissDistanceKmUnder();
        r.triggered = triggered;
        return r;
    }

    public Long getId() {
        return id;
    }

    public Long getAsteroidId() {
        return asteroidId;
    }

    public String getAsteroidName() {
        return asteroidName;
    }

    public String getLabel() {
        return label;
    }

    public boolean isAlertIfHazardous() {
        return alertIfHazardous;
    }

    public Double getAlertIfMissDistanceKmUnder() {
        return alertIfMissDistanceKmUnder;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
