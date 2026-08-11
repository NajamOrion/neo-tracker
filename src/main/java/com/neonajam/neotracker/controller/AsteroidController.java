package com.neonajam.neotracker.controller;

import com.neonajam.neotracker.dto.AsteroidResponse;
import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.repository.AsteroidRepository;
import com.neonajam.neotracker.service.AsteroidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asteroids")
public class AsteroidController {

    private final AsteroidService asteroidService;
    private final AsteroidRepository asteroidRepository;

    public AsteroidController(AsteroidService asteroidService, AsteroidRepository asteroidRepository) {
        this.asteroidService = asteroidService;
        this.asteroidRepository = asteroidRepository;
    }

    @PostMapping("/fetch")
    public Map<String, Object> fetch(@RequestParam String startDate,
                                     @RequestParam String endDate) {
        int count = asteroidService.fetchAndStore(startDate, endDate);
        return Map.of("stored", count, "startDate", startDate, "endDate", endDate);
    }

    @GetMapping
    public List<AsteroidResponse> list(
            @RequestParam(required = false) Boolean hazardous,
            @RequestParam(required = false) Double minDiameter) {

        List<Asteroid> asteroids;
        if (Boolean.TRUE.equals(hazardous)) {
            asteroids = asteroidRepository.findByPotentiallyHazardousTrue();
        } else if (minDiameter != null) {
            asteroids = asteroidRepository.findByEstimatedDiameterMinMetersGreaterThanEqual(minDiameter);
        } else {
            asteroids = asteroidRepository.findAll();
        }

        return asteroids.stream().map(AsteroidResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsteroidResponse> getOne(@PathVariable Long id) {
        return asteroidRepository.findById(id)
                .map(AsteroidResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hazardous")
    public List<AsteroidResponse> hazardous() {
        return asteroidRepository.findByPotentiallyHazardousTrue()
                .stream().map(AsteroidResponse::from).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (asteroidRepository.existsById(id)) {
            asteroidRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}