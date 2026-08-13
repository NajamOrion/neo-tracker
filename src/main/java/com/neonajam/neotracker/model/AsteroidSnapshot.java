package com.neonajam.neotracker.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class AsteroidSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asteroid_id", nullable = false)
    private Asteroid asteroid;

    private Instant capturedAt;

    public double estimatedDiameterMinMeters;
    public double estimatedDiameterMaxMeters;
    public boolean potentiallyHazardous;
    public Double nearestMissDistanceKm; // Nullable, as there may be no close approaches

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

    public Instant getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Instant capturedAt) {
        this.capturedAt = capturedAt;
    }

    public double getEstimatedDiameterMinMeters() {
        return estimatedDiameterMinMeters;
    }

    public void setEstimatedDiameterMinMeters(double estimatedDiameterMinMeters) {
        this.estimatedDiameterMinMeters = estimatedDiameterMinMeters;
    }

    public double getEstimatedDiameterMaxMeters() {
        return estimatedDiameterMaxMeters;
    }

    public void setEstimatedDiameterMaxMeters(double estimatedDiameterMaxMeters) {
        this.estimatedDiameterMaxMeters = estimatedDiameterMaxMeters;
    }

    public boolean isPotentiallyHazardous() {
        return potentiallyHazardous;
    }

    public void setPotentiallyHazardous(boolean potentiallyHazardous) {
        this.potentiallyHazardous = potentiallyHazardous;
    }

    public Double getNearestMissDistanceKm() {
        return nearestMissDistanceKm;
    }

    public void setNearestMissDistanceKm(Double nearestMissDistanceKm) {
        this.nearestMissDistanceKm = nearestMissDistanceKm;
    }
}
