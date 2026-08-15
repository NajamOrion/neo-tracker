package com.neonajam.neotracker.service;

import com.neonajam.neotracker.model.*;
import com.neonajam.neotracker.repository.AsteroidSnapshotRepository;
import com.neonajam.neotracker.repository.DataChangeLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjLongConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataQualityServiceTest {

    private DataChangeLogRepository dataChangeLogRepository;
    private AsteroidSnapshotRepository asteroidSnapshotRepository;
    private DataQualityService dataQualityService;

    @BeforeEach
    void setup() {
        dataChangeLogRepository = mock(DataChangeLogRepository.class);
        asteroidSnapshotRepository = mock(AsteroidSnapshotRepository.class);
        dataQualityService = new DataQualityService(asteroidSnapshotRepository, dataChangeLogRepository);
    }

    //Helper method that will be used to build an asteroid.
    //Only one approach needed to simulate not empty.
    private Asteroid asteroidWith(double dMin, double dMax, boolean hasApproach) {
        Asteroid a = new Asteroid();

        a.setEstimatedDiameterMinMeters(dMin);
        a.setEstimatedDiameterMaxMeters(dMax);

        List<CloseApproach> approaches = new ArrayList<>();

        if (hasApproach) {
            approaches.add(new CloseApproach());
        }

        a.setCloseApproaches(approaches);

        return a;
    }

    @Test
    void computeConfidence_completeStableData_ratesHigh() {
        Asteroid a = asteroidWith(100, 150, true);

        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(0L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertEquals(ConfidenceRating.HIGH, result.rating());
        assertTrue(result.reasons().isEmpty());
    }

    @Test
    void computeConfidence_missingApproachData_deductsAndRatesMedium() {
        Asteroid a = asteroidWith(100, 150, false);

        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(0L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertEquals(ConfidenceRating.MEDIUM, result.rating());
        assertTrue(result.reasons().contains("No close-approach data available."));
    }

    @Test
    void computeConfidence_invalidDiameterAndNoApproach_ratesLow() {
        Asteroid a = asteroidWith(0, 0, false);

        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(0L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertEquals(ConfidenceRating.LOW, result.rating());
        assertEquals(2, result.reasons().size());
    }

    @Test
    void computeConfidence_wideDiameterSpread_deductsUncertainty() {
        Asteroid a = asteroidWith(20, 200, true);

        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(0L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertTrue(result.reasons().contains("High diameter uncertainty"));
    }

    @Test
    void computeConfidence_narrowDiameterSpread_noUncertaintyDeduction() {
        Asteroid a = asteroidWith(100, 110, true);

        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(0L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertFalse(result.reasons().contains("High diameter uncertainty"));
    }

    @Test
    void computeConfidence_dataRevisedMultipleTimes_flagsInstability() {
        Asteroid a = asteroidWith(100, 150, true);

        //Data revised 3 times: 3L
        when(dataChangeLogRepository.countByAsteroid(a)).thenReturn(3L);
        when(dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(a)).thenReturn(new ArrayList<>());

        DataQualityService.ConfidenceResult result = dataQualityService.computeConfidence(a);

        assertTrue(result.reasons().contains("Data reviewed multiple times"));
    }

    @Test
    void detectAndLog_firstFetch_createNoChangeLog() {
        Asteroid a = asteroidWith(100, 150, true);

        when(asteroidSnapshotRepository.findTopByAsteroidOrderByCapturedAtDesc(a))
                .thenReturn(java.util.Optional.empty());

        dataQualityService.detectAndLog(a, 100, 150, true, 1_000_000.0);

        verify(dataChangeLogRepository, never()).save(any());

        verify(asteroidSnapshotRepository, times(1)).save(any());
    }

    @Test
    void detectAndLog_noChange_createsNoLog() {
        Asteroid a = asteroidWith(100, 150, false);

        AsteroidSnapshot last = new AsteroidSnapshot();

        last.setEstimatedDiameterMinMeters(100);
        last.setEstimatedDiameterMaxMeters(150);
        last.setPotentiallyHazardous(false);
        last.setNearestMissDistanceKm(1_000_000.0);

        when(asteroidSnapshotRepository.findTopByAsteroidOrderByCapturedAtDesc(a))
                .thenReturn(java.util.Optional.of(last));

        dataQualityService.detectAndLog(a, 100, 150, false, 1_000_000.0);

        verify(dataChangeLogRepository, never()).save(any());
    }

    @Test
    void detectAndLog_hazardFlagChanged_logsReclassification() {
        Asteroid a = asteroidWith(100, 150, true);

        AsteroidSnapshot last = new AsteroidSnapshot();

        last.setEstimatedDiameterMinMeters(100);
        last.setEstimatedDiameterMaxMeters(150);
        last.setPotentiallyHazardous(false);
        last.setNearestMissDistanceKm(1_000_000.0);

        when(asteroidSnapshotRepository.findTopByAsteroidOrderByCapturedAtDesc(a))
                .thenReturn(java.util.Optional.of(last));

        dataQualityService.detectAndLog(a, 100, 150, true, 1_000_000.0);

        verify(dataChangeLogRepository, times(1)).save(any());
    }

    @Test
    void detectAndLog_diameterChanged_logsRevision() {
        Asteroid a = asteroidWith(200, 260, false);

        AsteroidSnapshot last = new AsteroidSnapshot();

        last.setEstimatedDiameterMinMeters(100);
        last.setEstimatedDiameterMaxMeters(150);
        last.setPotentiallyHazardous(false);
        last.setNearestMissDistanceKm(1_000_000.0);

        when(asteroidSnapshotRepository.findTopByAsteroidOrderByCapturedAtDesc(a))
                .thenReturn(java.util.Optional.of(last));

        dataQualityService.detectAndLog(a, 200, 260, false, 1_000_000.0);

        verify(dataChangeLogRepository, times(1)).save(any());
    }
}
