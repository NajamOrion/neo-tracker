package com.neonajam.neotracker.controller;

import com.neonajam.neotracker.dto.ChangeLogResponse;
import com.neonajam.neotracker.model.ConfidenceRating;
import com.neonajam.neotracker.dto.DataQualityResponse;
import com.neonajam.neotracker.repository.AsteroidRepository;
import com.neonajam.neotracker.repository.DataChangeLogRepository;
import com.neonajam.neotracker.service.DataQualityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/asteroids")
public class DataQualityController {

    private final AsteroidRepository asteroidRepository;
    private final DataChangeLogRepository dataChangeLogRepository;
    private final DataQualityService dataQualityService;

    public DataQualityController(AsteroidRepository asteroidRepository,
                                 DataChangeLogRepository dataChangeLogRepository,
                                 DataQualityService dataQualityService) {
        this.asteroidRepository = asteroidRepository;
        this.dataChangeLogRepository = dataChangeLogRepository;
        this.dataQualityService = dataQualityService;
    }

    //Change history for one asteroid
    @GetMapping("{id}/history")
    public ResponseEntity<List<ChangeLogResponse>> history (@PathVariable Long id) {
        return asteroidRepository.findById(id)
                .map(asteroid -> ResponseEntity.ok(
                        dataChangeLogRepository.findByAsteroidOrderByChangedAtDesc(asteroid)
                                .stream().map(ChangeLogResponse::from).toList()))
                .orElse(ResponseEntity.notFound().build());
    }

    //Get asteroids that had changes since a given date (e.g. ?since=2026-08-01)
    @GetMapping("/changed")
    public List<ChangeLogResponse> changedSince(@RequestParam String since) {
        Instant sinceInstant = LocalDate.parse(since).atStartOfDay().toInstant(ZoneOffset.UTC);

        return dataChangeLogRepository.findByChangedAtAfter(sinceInstant)
                .stream().map(ChangeLogResponse::from).toList();
    }

    //Confidence rating and reasons for a single asteroid
    @GetMapping("/{id}/quality")
    public ResponseEntity<DataQualityResponse> quality(@PathVariable Long id) {
        return asteroidRepository.findById(id)
                .map(asteroid -> {
                    DataQualityService.ConfidenceResult result =
                            dataQualityService.computeConfidence(asteroid);
                    return  ResponseEntity.ok(new DataQualityResponse(
                            asteroid.getId(), asteroid.getName(),
                            result.rating(), result.reasons()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/low-confidence")
    public List<DataQualityResponse> lowConfidence() {
        return asteroidRepository.findAll().stream()
                .map(a -> {
                    DataQualityService.ConfidenceResult result =
                            dataQualityService.computeConfidence(a);
                    return new DataQualityResponse(a.getId(), a.getName(),
                            result.rating(), result.reasons());
                })
                .filter(r -> r.getConfidenceRating() == ConfidenceRating.LOW)
                .toList();
    }
}
