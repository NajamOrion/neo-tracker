package com.neonajam.neotracker.controller;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.SizeClass;
import com.neonajam.neotracker.repository.AsteroidRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final AsteroidRepository asteroidRepository;

    public TestController(AsteroidRepository asteroidRepository) {
        this.asteroidRepository = asteroidRepository;
    }

    @PostMapping("/asteroid")
    public Asteroid createTestAsteroid() {
        Asteroid a = new Asteroid();
        a.setNeoReferenceId("test-123");
        a.setName("Test Asteroid");
        a.setAbsoluteMagnitude(21.5);
        a.setEstimatedDiameterMinMeters(100);
        a.setEstimatedDiameterMaxMeters(250);
        a.setPotentiallyHazardous(true);
        a.setSizeClass(SizeClass.LARGE);
        a.setFirstStoredAt(Instant.now());
        a.setLastCheckedAt(Instant.now());
        return asteroidRepository.save(a);
    }

    @GetMapping("/asteroids")
    public List<Asteroid> getAllTestAsteroids() {
        return asteroidRepository.findAll();
    }
}
