package com.neonajam.neotracker.dto;

import com.neonajam.neotracker.model.CloseApproach;

import java.time.LocalDate;

public class CloseApproachResponse {

    private LocalDate approachDate;
    private double relativeVelocityKph;
    private double missDistanceKm;
    private String orbitingBody;

    public static CloseApproachResponse from(CloseApproach c) {
        CloseApproachResponse r = new CloseApproachResponse();
        r.approachDate = c.getApproachDate();
        r.relativeVelocityKph = c.getRelativeVelocityKph();
        r.missDistanceKm = c.getMissDistanceKm();
        r.orbitingBody = c.getOrbitingBody();
        return r;
    }

    public LocalDate getApproachDate() {
        return approachDate;
    }

    public double getRelativeVelocityKph() {
        return relativeVelocityKph;
    }

    public double getMissDistanceKm() {
        return missDistanceKm;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }
}
