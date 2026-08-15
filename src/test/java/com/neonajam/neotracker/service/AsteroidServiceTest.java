package com.neonajam.neotracker.service;

import com.neonajam.neotracker.exception.InvalidDateRangeException;
import com.neonajam.neotracker.repository.AsteroidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AsteroidServiceTest {

    private NasaClient nasaClient;
    private AsteroidRepository asteroidRepository;
    private DataQualityService dataQualityService;
    private AsteroidService asteroidService;

    @BeforeEach
    void setup() {
        nasaClient = mock(NasaClient.class);
        asteroidRepository = mock(AsteroidRepository.class);
        dataQualityService = mock(DataQualityService.class);
        asteroidService = new AsteroidService(nasaClient, asteroidRepository, dataQualityService);
    }

    @Test
    void fetchAndStore_rangeOver7Days_throwsInvalidDateRange() {
        assertThrows(InvalidDateRangeException.class,
                () -> asteroidService.fetchAndStore("2026-08-01","2026-08-20"));
    }

    @Test
    void fetchAndStore_reverseDates_throwsInvalidDateRange() {
        assertThrows(InvalidDateRangeException.class,
                () -> asteroidService.fetchAndStore("2026-08-08","2026-08-01"));
    }

    @Test
    void fetchAndStore_rangeOver7Days_neverCallsNasa() {
        assertThrows(InvalidDateRangeException.class,
                () -> asteroidService.fetchAndStore("2026-08-01","2026-08-20"));

        verify(nasaClient, never()).getFeed(anyString(), anyString());
    }
}
