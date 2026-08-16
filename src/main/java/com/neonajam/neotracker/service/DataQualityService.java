package com.neonajam.neotracker.service;

import com.neonajam.neotracker.model.*;
import com.neonajam.neotracker.repository.AsteroidSnapshotRepository;
import com.neonajam.neotracker.repository.DataChangeLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataQualityService {

    private final AsteroidSnapshotRepository asteroidSnapshotRepository;
    private final DataChangeLogRepository changeLogRepository;

    public DataQualityService(AsteroidSnapshotRepository asteroidSnapshotRepository, DataChangeLogRepository changeLogRepository) {
        this.asteroidSnapshotRepository = asteroidSnapshotRepository;
        this.changeLogRepository = changeLogRepository;
    }

    //Change detection.
    //The aim is to compare the incoming data against the last snapshot, log any changes
    //and then save a new snapshot for future comparisons.
    //Called during the fetch and is before the overwrite of the entity data.
    public void detectAndLog(Asteroid existing, double newDiameterMin,
                             double newDiameterMax, boolean newHazardous,
                             Double newNearestMiss) {

        Optional<AsteroidSnapshot> lastSnapshotOpt = asteroidSnapshotRepository.findTopByAsteroidOrderByCapturedAtDesc(existing);

        if (lastSnapshotOpt.isPresent()) {
            AsteroidSnapshot last = lastSnapshotOpt.get();

            if (last.isPotentiallyHazardous() != newHazardous) {
                logChange(existing, "potentiallyHazardous",
                        String.valueOf(last.isPotentiallyHazardous()),
                        String.valueOf(newHazardous),
                        ChangeType.HAZARD_RECLASSIFIED);
            }

            if (last.getEstimatedDiameterMinMeters() != newDiameterMin
                || last.getEstimatedDiameterMaxMeters() != newDiameterMax) {
                logChange(existing, "estimatedDiameter",
                        last.getEstimatedDiameterMinMeters() + "-" + last.getEstimatedDiameterMaxMeters(),
                        newDiameterMin + "-" + newDiameterMax,
                        ChangeType.DIAMETER_REVISED);
            }

            if (!java.util.Objects.equals(last.getNearestMissDistanceKm(), newNearestMiss)) {
                logChange(existing, "nearestMissDistanceKm",
                        String.valueOf(last.getNearestMissDistanceKm()),
                        String.valueOf(newNearestMiss),
                        ChangeType.DISTANCE_REVISED);
            }
        }

        AsteroidSnapshot snapshot = new AsteroidSnapshot();
        snapshot.setAsteroid(existing);
        snapshot.setCapturedAt(Instant.now());
        snapshot.setEstimatedDiameterMinMeters(newDiameterMin);
        snapshot.setEstimatedDiameterMaxMeters(newDiameterMax);
        snapshot.setPotentiallyHazardous(newHazardous);
        snapshot.setNearestMissDistanceKm(newNearestMiss);
        asteroidSnapshotRepository.save(snapshot);
    }

    public void logChange(Asteroid asteroid, String fieldName, String oldValue,
                          String newValue, ChangeType type) {
        DataChangeLog log = new DataChangeLog();
        log.setAsteroid(asteroid);
        log.setChangedAt(Instant.now());
        log.setFieldName(fieldName);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setChangeType(type);
        changeLogRepository.save(log);
    }

    public ConfidenceResult computeConfidence(Asteroid asteroid) {
        int score = 100;
        List<String> reasons = new ArrayList<>();

        if (asteroid.getCloseApproaches() == null || asteroid.getCloseApproaches().isEmpty()) {
            score -= 40;
            reasons.add("No close-approach data available.");
        }

        double diameterMin = asteroid.getEstimatedDiameterMinMeters();
        double diameterMax = asteroid.getEstimatedDiameterMaxMeters();

        if (diameterMin <= 0 || diameterMax <= 0) {
            score -= 30;
            reasons.add("Estimated diameter data is missing or invalid.");
        }

        long changeCount = changeLogRepository.countByAsteroid(asteroid);

        if (changeCount >= 2) {
            score -= 15;
            reasons.add("Data reviewed multiple times");
        }

        boolean hazardReclassified = changeLogRepository
                .findByAsteroidOrderByChangedAtDesc(asteroid).stream()
                .anyMatch(c -> c.getChangeType() == ChangeType.HAZARD_RECLASSIFIED);

        if (hazardReclassified) {
            score -= 15;
            reasons.add("Hazard status recently reclassified");
        }

        ConfidenceRating rating;

        if (score >= 70) {
            rating = ConfidenceRating.HIGH;
        } else if (score >= 40) {
            rating = ConfidenceRating.MEDIUM;
        } else {
            rating = ConfidenceRating.LOW;
        }

        return new ConfidenceResult(rating, reasons);
    }

    public record ConfidenceResult (ConfidenceRating rating, List<String> reasons) {}
}