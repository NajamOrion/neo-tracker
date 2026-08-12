package com.neonajam.neotracker.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asteroid_id", nullable = false)
    private Asteroid asteroid;

    private String label;
    private boolean alertIfHazardous;
    private Double alertIfMissDistanceKmUnder;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }

    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
