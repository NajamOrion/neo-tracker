package com.neonajam.neotracker.dto;

public class WatchRequest {

    private Long asteroidId;
    private String label;
    private boolean alertIfHazardous;
    private Double alertIfMissDistanceKmUnder;

    public Long getAsteroidId() {
        return asteroidId;
    }

    public void setAsteroidId(Long asteroidId) {
        this.asteroidId = asteroidId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAlertIfHazardous() {
        return alertIfHazardous;
    }

    public void setAlertIfHazardous(boolean alertIfHazardous) {
        this.alertIfHazardous = alertIfHazardous;
    }

    public Double getAlertIfMissDistanceKmUnder() {
        return alertIfMissDistanceKmUnder;
    }

    public void setAlertIfMissDistanceKmUnder(Double alertIfMissDistanceKmUnder) {
        this.alertIfMissDistanceKmUnder = alertIfMissDistanceKmUnder;
    }
}
