package com.neonajam.neotracker.dto;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.ConfidenceRating;
import com.neonajam.neotracker.model.SizeClass;

import java.util.List;

public class AsteroidResponse {

    private Long id;
    private String neoReferenceId;
    private String name;
    private double estimatedDiameterMinMeters;
    private double estimatedDiameterMaxMeters;
    private boolean potentiallyHazardous;
    private SizeClass sizeClass;
    private ConfidenceRating confidenceRating;
    private List<CloseApproachResponse> closeApproaches;

    // A static factory: build a response from an entity.
    public static AsteroidResponse from(Asteroid a) {
        AsteroidResponse r = new AsteroidResponse();
        r.id = a.getId();
        r.neoReferenceId = a.getNeoReferenceId();
        r.name = a.getName();
        r.estimatedDiameterMinMeters = a.getEstimatedDiameterMinMeters();
        r.estimatedDiameterMaxMeters = a.getEstimatedDiameterMaxMeters();
        r.potentiallyHazardous = a.isPotentiallyHazardous();
        r.sizeClass = a.getSizeClass();
        r.confidenceRating = a.getConfidenceRating();
        r.closeApproaches = a.getCloseApproaches().stream()
                .map(CloseApproachResponse::from)
                .toList();
        return r;
    }

    public Long getId() {
        return id;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public String getName() {
        return name;
    }

    public double getEstimatedDiameterMinMeters() {
        return estimatedDiameterMinMeters;
    }

    public double getEstimatedDiameterMaxMeters() {
        return estimatedDiameterMaxMeters;
    }

    public boolean isPotentiallyHazardous() {
        return potentiallyHazardous;
    }

    public SizeClass getSizeClass() {
        return sizeClass;
    }

    public ConfidenceRating getConfidenceRating() {
        return confidenceRating;
    }

    public List<CloseApproachResponse> getCloseApproaches() {
        return closeApproaches;
    }
}
