package com.neonajam.neotracker.controller;

import com.neonajam.neotracker.service.AsteroidService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/asteroids")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @PostMapping("/fetch")
    public Map<String, Object> fetch(@RequestParam String startDate,
                                     @RequestParam String endDate) {
        int count = asteroidService.fetchAndStore(startDate, endDate);
        return Map.of("stored", count, "startDate", startDate, "endDate", endDate);
    }
}