package com.neonajam.neotracker.dto;

import com.neonajam.neotracker.model.ConfidenceRating;

import java.util.List;

public class DataQualityResponse {

    private Long asteroidId;
    private String asteroidName;
    private ConfidenceRating confidenceRating;
    private List<String> reasons;

    public DataQualityResponse(Long asteroidId, String asteroidName,
                              ConfidenceRating rating, List<String> reasons) {
        this.asteroidId = asteroidId;
        this.asteroidName = asteroidName;
        this.confidenceRating = rating;
        this.reasons = reasons;
    }

    public Long getAsteroidId() {
        return asteroidId;
    }

    public String getAsteroidName() {
        return asteroidName;
    }

    public ConfidenceRating getConfidenceRating() {
        return confidenceRating;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
