package com.neonajam.neotracker.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaDiameterMeters {

    @JsonProperty("estimated_diameter_min")
    private double estimatedDiameterMin;

    @JsonProperty("estimated_diameter_max")
    private double estimatedDiameterMax;

    public double getEstimatedDiameterMin() {
        return estimatedDiameterMin;
    }

    public void setEstimatedDiameterMin(double estimatedDiameterMin) {
        this.estimatedDiameterMin = estimatedDiameterMin;
    }

    public double getEstimatedDiameterMax() {
        return estimatedDiameterMax;
    }

    public void setEstimatedDiameterMax(double estimatedDiameterMax) {
        this.estimatedDiameterMax = estimatedDiameterMax;
    }
}
