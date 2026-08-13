package com.neonajam.neotracker.repository;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.AsteroidSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsteroidSnapshotRepository extends JpaRepository<AsteroidSnapshot, Long> {

    // Find the most recent snapshot for a given asteroid
    Optional<AsteroidSnapshot> findTopByAsteroidOrderByCapturedAtDesc(Asteroid asteroid);
}