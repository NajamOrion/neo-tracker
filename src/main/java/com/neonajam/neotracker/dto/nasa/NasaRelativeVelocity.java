package com.neonajam.neotracker.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaRelativeVelocity {

    @JsonProperty("kilometers_per_hour")
    private String kilometersPerHour;

    public String getKilometersPerHour() {
        return kilometersPerHour;
    }

    public void setKilometersPerHour(String kilometersPerHour) {
        this.kilometersPerHour = kilometersPerHour;
    }
}
