package com.neonajam.neotracker.repository;

import com.neonajam.neotracker.model.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchRepository extends JpaRepository<Watch, Long> {
}
