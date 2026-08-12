package com.neonajam.neotracker.controller;

import com.neonajam.neotracker.dto.WatchRequest;
import com.neonajam.neotracker.dto.WatchResponse;
import com.neonajam.neotracker.model.Watch;
import com.neonajam.neotracker.repository.WatchRepository;
import com.neonajam.neotracker.service.WatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watches")
public class WatchController {

    private final WatchService watchService;
    private final WatchRepository watchRepository;

    public WatchController(WatchService watchService, WatchRepository watchRepository) {
        this.watchService = watchService;
        this.watchRepository = watchRepository;
    }

    @PostMapping
    public WatchResponse create(@RequestBody WatchRequest request) {
        Watch watch = watchService.create(
                request.getAsteroidId(),
                request.getLabel(),
                request.isAlertIfHazardous(),
                request.getAlertIfMissDistanceKmUnder());
        return WatchResponse.from(watch, watchService.isTriggered(watch));
    }

    @GetMapping
    public List<WatchResponse> list() {
        return watchRepository.findAll().stream()
                .map(w -> WatchResponse.from(w,watchService.isTriggered(w)))
                .toList();
    }

    @GetMapping("/triggered")
    public List<WatchResponse> triggered() {
        return watchRepository.findAll().stream()
                .filter(watchService::isTriggered)
                .map(w -> WatchResponse.from(w, true))
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (watchRepository.existsById(id)) {
            watchRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
