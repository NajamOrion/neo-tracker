package com.neonajam.neotracker.service;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.CloseApproach;
import com.neonajam.neotracker.model.Watch;
import com.neonajam.neotracker.repository.AsteroidRepository;
import com.neonajam.neotracker.repository.WatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class WatchServiceTest {

    private WatchService watchService;

    @BeforeEach
    void setUp() {
        watchService = new WatchService(mock(WatchRepository.class), mock(AsteroidRepository.class));
    }

    //Helper to create an asteroid with a hazard flag and one approach at a given distance
    private Asteroid asteroid(boolean hazardous, double missDistanceKm) {
        Asteroid a = new Asteroid();
        a.setPotentiallyHazardous(hazardous);

        List<CloseApproach> approaches = new ArrayList<>();

        CloseApproach ca = new CloseApproach();
        ca.setMissDistanceKm(missDistanceKm);

        approaches.add(ca);
        a.setCloseApproaches(approaches);

        return a;
    }

    private Watch watchFor(Asteroid a, boolean alertIfHazardous, Double missThreshold) {
        Watch w = new Watch();

        w.setAsteroid(a);
        w.setAlertIfHazardous(alertIfHazardous);
        w.setAlertIfMissDistanceKmUnder(missThreshold);

        return w;
    }

    @Test
    void isTriggered_hazardousAsteroidWithHazardAlert_returnsTrue() {
        Watch w = watchFor(asteroid(true, 5_000_000), true, null);
        assertTrue(watchService.isTriggered(w));
    }

    @Test
    void isTriggered_nonHazardousAsteroidWithHazardAlert_returnsFalse() {
        Watch w = watchFor(asteroid(false, 5_000_000), true, null);
        assertFalse(watchService.isTriggered(w));
    }

    @Test
    void isTriggered_missDistanceUnderThreshold_returnsTrue() {
        Watch w = watchFor(asteroid(false, 1000), false, 5000.0);
        assertTrue(watchService.isTriggered(w));
    }

    @Test
    void isTriggered_missDistanceOverThreshold_returnsFalse() {
        Watch w = watchFor(asteroid(false, 10_000), false, 5000.0);
        assertFalse(watchService.isTriggered(w));
    }

    @Test
    void isTriggered_missDistanceExactlyAtThreshold_returnsFalse() {
        Watch w = watchFor(asteroid(false, 5000), false, 5000.0);
        assertFalse(watchService.isTriggered(w));
    }
}
