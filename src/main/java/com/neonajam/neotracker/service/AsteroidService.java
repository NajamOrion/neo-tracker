package com.neonajam.neotracker.service;

import com.neonajam.neotracker.dto.nasa.NasaAsteroid;
import com.neonajam.neotracker.dto.nasa.NasaCloseApproach;
import com.neonajam.neotracker.dto.nasa.NasaFeedResponse;
import com.neonajam.neotracker.exception.InvalidDateRangeException;
import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.CloseApproach;
import com.neonajam.neotracker.model.SizeClass;
import com.neonajam.neotracker.repository.AsteroidRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AsteroidService {

    private final NasaClient nasaClient;
    private final AsteroidRepository asteroidRepository;
    private final DataQualityService dataQualityService;

    public AsteroidService(NasaClient nasaClient, AsteroidRepository asteroidRepository,
                           DataQualityService dataQualityService) {
        this.nasaClient = nasaClient;
        this.asteroidRepository = asteroidRepository;
        this.dataQualityService = dataQualityService;
    }

    public int fetchAndStore(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        //Validate that the date range is within 7 days and that the start date is not after the end date
        if (end.isBefore(start)) {
            throw new InvalidDateRangeException("End date must not be before start date");
        }

        if (ChronoUnit.DAYS.between(start, end) > 7) {
            throw new InvalidDateRangeException("Date range cannot exceed 7 days");
        }

        NasaFeedResponse feed = nasaClient.getFeed(startDate, endDate);

        int storedCount = 0;

        for(Map.Entry<String, List<NasaAsteroid>> entry : feed.getNearEarthObjects().entrySet()) {
            for(NasaAsteroid nasaAsteroid : entry.getValue()) {
                storeOrUpdate(nasaAsteroid);
                storedCount++;
            }
        }

        return storedCount;
    }

    public void storeOrUpdate(NasaAsteroid nasaAsteroid) {
        // Check if asteroid exists in the database by its neoReferenceId,
        // then update it if it does, or create a new entry if it doesn't
        Optional<Asteroid> existing = asteroidRepository.findByNeoReferenceId(nasaAsteroid.getNeoReferenceId());

        Asteroid asteroid = existing.orElseGet(Asteroid::new);

        //New values from NASA before overwriting.
        double newDiameterMin = nasaAsteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMin();
        double newDiameterMax = nasaAsteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMax();
        boolean newHazardous = nasaAsteroid.isPotentiallyHazardous();
        Double newNearestMiss = computeNearestMiss(nasaAsteroid);

        if (existing.isPresent()) {
            dataQualityService.detectAndLog(asteroid, newDiameterMin, newDiameterMax,
                    newHazardous, newNearestMiss);
        }

        asteroid.setNeoReferenceId(nasaAsteroid.getNeoReferenceId());
        asteroid.setName(nasaAsteroid.getName());
        asteroid.setAbsoluteMagnitude(nasaAsteroid.getAbsoluteMagnitudeH());
        asteroid.setEstimatedDiameterMinMeters(
                nasaAsteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMin());
        asteroid.setEstimatedDiameterMaxMeters(
                nasaAsteroid.getEstimatedDiameter().getMeters().getEstimatedDiameterMax());
        asteroid.setSizeClass(SizeClass.fromDiameter(
                asteroid.getEstimatedDiameterMinMeters(),
                asteroid.getEstimatedDiameterMaxMeters()));
        asteroid.setPotentiallyHazardous(nasaAsteroid.isPotentiallyHazardous());

        Instant now = Instant.now();

        if (existing.isEmpty()) {
            asteroid.setFirstStoredAt(now);
        }

        asteroid.setLastCheckedAt(now);

        // Map the close approaches from the NASA response to the Asteroid entity
        asteroid.getCloseApproaches().clear();
        if (nasaAsteroid.getCloseApproachData() != null) {
            for (NasaCloseApproach nasaApproach : nasaAsteroid.getCloseApproachData()) {
                CloseApproach approach = new CloseApproach();
                approach.setApproachDate(LocalDate.parse(nasaApproach.getCloseApproachDate()));
                approach.setRelativeVelocityKph(
                        Double.parseDouble(nasaApproach.getRelativeVelocity().getKilometersPerHour()));
                approach.setMissDistanceKm(
                        Double.parseDouble(nasaApproach.getMissDistance().getKilometers()));
                approach.setOrbitingBody(nasaApproach.getOrbitingBody());
                approach.setAsteroid(asteroid);
                asteroid.getCloseApproaches().add(approach);
            }
        }

        Asteroid saved =  asteroidRepository.save(asteroid);

        DataQualityService.ConfidenceResult confidence = dataQualityService.computeConfidence(saved);
        saved.setConfidenceRating(confidence.rating());
        asteroidRepository.save(saved);

        // There is no prior snapshot for a new asteroid,
        // so the new data is saved for this purpose.
        if (existing.isEmpty()) {
            dataQualityService.detectAndLog(saved, newDiameterMin, newDiameterMax, newHazardous,
                    newNearestMiss);
        }
    }

    private Double computeNearestMiss (NasaAsteroid nasaAsteroid) {
        if (nasaAsteroid.getCloseApproachData() == null || nasaAsteroid.getCloseApproachData().isEmpty()) {
            return null;
        }

        return nasaAsteroid.getCloseApproachData().stream()
                .mapToDouble(a -> Double.parseDouble(a.getMissDistance().getKilometers()))
                .min()
                .orElse(Double.MAX_VALUE);
    }
}
