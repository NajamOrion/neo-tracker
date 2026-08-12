package com.neonajam.neotracker.service;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.CloseApproach;
import com.neonajam.neotracker.model.Watch;
import com.neonajam.neotracker.repository.AsteroidRepository;
import com.neonajam.neotracker.repository.WatchRepository;
import org.springframework.stereotype.Service;

@Service
public class WatchService {

    private final WatchRepository watchRepository;
    private final AsteroidRepository asteroidRepository;

    public WatchService(WatchRepository watchRepository, AsteroidRepository asteroidRepository) {
        this.watchRepository = watchRepository;
        this.asteroidRepository = asteroidRepository;
    }

    public Watch create(Long asteroidId, String label, boolean alertIfHazardous,
                        Double alertIfMissDistanceKmUnder) {
        Asteroid asteroid = asteroidRepository.findById(asteroidId)
                .orElseThrow(() -> new IllegalArgumentException("Asteroid not found with id: " + asteroidId));

        Watch watch = new Watch();
        watch.setAsteroid(asteroid);
        watch.setLabel(label);
        watch.setAlertIfHazardous(alertIfHazardous);
        watch.setAlertIfMissDistanceKmUnder(alertIfMissDistanceKmUnder);
        watch.setCreatedAt(java.time.Instant.now());

        return watchRepository.save(watch);
    }

    // Alert condition logic: check if watch is currently triggered.
    public boolean isTriggered(Watch watch) {
        Asteroid asteroid = watch.getAsteroid();

        if (watch.isAlertIfHazardous() && asteroid.isPotentiallyHazardous()) {
            return true;
        }

        if (watch.getAlertIfMissDistanceKmUnder() != null) {
            double miss = nearestMissDistance(asteroid);
            if (miss < watch.getAlertIfMissDistanceKmUnder()) {
                return true;
            }
        }

        return false;
    }

    private double nearestMissDistance(Asteroid asteroid) {
        return asteroid.getCloseApproaches().stream()
                .mapToDouble(CloseApproach::getMissDistanceKm)
                .min()
                .orElse(Double.MAX_VALUE);
    }
}
