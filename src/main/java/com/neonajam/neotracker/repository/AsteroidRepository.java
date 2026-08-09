package com.neonajam.neotracker.repository;

import com.neonajam.neotracker.model.Asteroid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AsteroidRepository extends JpaRepository<Asteroid, Long> {

    Optional<Asteroid> findByNeoReferenceId(String neoReferenceId);
}
