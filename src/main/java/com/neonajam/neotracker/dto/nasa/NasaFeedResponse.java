package com.neonajam.neotracker.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class NasaFeedResponse {

    @JsonProperty("element_count")
    private int elementCount;

    @JsonProperty("near_earth_objects")
    private Map<String, List<NasaAsteroid>> nearEarthObjects;

    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    public Map<String, List<NasaAsteroid>> getNearEarthObjects() {
        return nearEarthObjects;
    }

    public void setNearEarthObjects(Map<String, List<NasaAsteroid>> nearEarthObjects) {
        this.nearEarthObjects = nearEarthObjects;
    }
}
