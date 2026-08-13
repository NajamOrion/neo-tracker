package com.neonajam.neotracker.repository;

import com.neonajam.neotracker.model.Asteroid;
import com.neonajam.neotracker.model.DataChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DataChangeLogRepository extends JpaRepository<DataChangeLog, Long> {
    List<DataChangeLog> findByAsteroidOrderByChangedAtDesc(Asteroid asteroid);
    List<DataChangeLog> findByChangedAtAfter(Instant since);
    long countByAsteroid(Asteroid asteroid);
}