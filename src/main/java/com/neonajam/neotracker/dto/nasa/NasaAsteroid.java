package com.neonajam.neotracker.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NasaAsteroid {

    @JsonProperty("neo_reference_id")
    private String neoReferenceId;

    private String name;

    @JsonProperty("absolute_magnitude_h")
    private double absoluteMagnitudeH;

    @JsonProperty("estimated_diameter")
    private NasaEstimatedDiameter estimatedDiameter;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private boolean isPotentiallyHazardous;

    @JsonProperty("close_approach_data")
    private List<NasaCloseApproach> closeApproachData;

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

    public double getAbsoluteMagnitudeH() {
        return absoluteMagnitudeH;
    }

    public void setAbsoluteMagnitudeH(double absoluteMagnitudeH) {
        this.absoluteMagnitudeH = absoluteMagnitudeH;
    }

    public NasaEstimatedDiameter getEstimatedDiameter() {
        return estimatedDiameter;
    }

    public void setEstimatedDiameter(NasaEstimatedDiameter estimatedDiameter) {
        this.estimatedDiameter = estimatedDiameter;
    }

    public boolean isPotentiallyHazardous() {
        return isPotentiallyHazardous;
    }

    public void setPotentiallyHazardous(boolean potentiallyHazardous) {
        isPotentiallyHazardous = potentiallyHazardous;
    }

    public List<NasaCloseApproach> getCloseApproachData() {
        return closeApproachData;
    }

    public void setCloseApproachData(List<NasaCloseApproach> closeApproachData) {
        this.closeApproachData = closeApproachData;
    }
}
