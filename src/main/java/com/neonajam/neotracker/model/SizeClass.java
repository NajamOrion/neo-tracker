package com.neonajam.neotracker.model;

public enum SizeClass {
    SMALL,
    MEDIUM,
    LARGE;

    //Derive the size class from the estimated diameter in meters.
    //Classification is based on the midpoint of the estimated diameter range.
    // Small: < 50 meters
    // Medium: 50 - < 140 meters
    // Large: >= 140 meters
    //The 140-meter threshold (NASA's threshold for PHO) is based on the fact that asteroids larger than 140 meters can cause significant damage if they impact Earth.
    //The 50-meter threshold is based on the Tunguska event where the object was approximately 40 meters.
    public static SizeClass fromDiameter(double minMeters, double maxMeters) {
        double midpoint = (minMeters + maxMeters) / 2;

        if (midpoint < 50) {
            return SMALL;
        } else if (midpoint < 140) {
            return MEDIUM;
        } else {
            return LARGE;
        }
    }
}