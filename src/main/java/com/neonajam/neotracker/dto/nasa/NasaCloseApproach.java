package com.neonajam.neotracker.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaCloseApproach {

    @JsonProperty("close_approach_date")
    private String closeApproachDate;

    @JsonProperty("relative_velocity")
    private NasaRelativeVelocity relativeVelocity;

    @JsonProperty("miss_distance")
    private NasaMissDistance missDistance;

    @JsonProperty("orbiting_body")
    private String orbitingBody;

    public String getCloseApproachDate() {
        return closeApproachDate;
    }

    public void setCloseApproachDate(String closeApproachDate) {
        this.closeApproachDate = closeApproachDate;
    }

    public NasaRelativeVelocity getRelativeVelocity() {
        return relativeVelocity;
    }

    public void setRelativeVelocity(NasaRelativeVelocity relativeVelocity) {
        this.relativeVelocity = relativeVelocity;
    }

    public NasaMissDistance getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(NasaMissDistance missDistance) {
        this.missDistance = missDistance;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public void setOrbitingBody(String orbitingBody) {
        this.orbitingBody = orbitingBody;
    }
}
