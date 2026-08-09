package com.neonajam.neotracker.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Asteroid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String neoReferenceId;

    private String name;
    private double absoluteMagnitude;
    private double estimatedDiameterMinMeters;
    private double estimatedDiameterMaxMeters;
    private boolean potentiallyHazardous;

    @Enumerated(EnumType.STRING)
    private SizeClass sizeClass;

    @Enumerated(EnumType.STRING)
    private ConfidenceRating confidenceRating;

    private Instant firstStoredAt;
    private Instant lastCheckedAt;

    @OneToMany(mappedBy = "asteroid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CloseApproach> closeApproaches = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    public void setAbsoluteMagnitude(double absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;
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

    public SizeClass getSizeClass() {
        return sizeClass;
    }

    public void setSizeClass(SizeClass sizeClass) {
        this.sizeClass = sizeClass;
    }

    public ConfidenceRating getConfidenceRating() {
        return confidenceRating;
    }

    public void setConfidenceRating(ConfidenceRating confidenceRating) {
        this.confidenceRating = confidenceRating;
    }

    public Instant getFirstStoredAt() {
        return firstStoredAt;
    }

    public void setFirstStoredAt(Instant firstStoredAt) {
        this.firstStoredAt = firstStoredAt;
    }

    public Instant getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(Instant lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public List<CloseApproach> getCloseApproaches() {
        return closeApproaches;
    }

    public void setCloseApproaches(List<CloseApproach> closeApproaches) {
        this.closeApproaches = closeApproaches;
    }
}
