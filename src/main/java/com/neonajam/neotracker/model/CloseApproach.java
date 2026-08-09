package com.neonajam.neotracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CloseApproach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate approachDate;
    private double missDistanceKm;
    private double relativeVelocityKph;
    private String orbitingBody;

    @ManyToOne
    @JoinColumn(name = "asteroid_id")
    private Asteroid asteroid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApproachDate() {
        return approachDate;
    }

    public void setApproachDate(LocalDate approachDate) {
        this.approachDate = approachDate;
    }

    public double getMissDistanceKm() {
        return missDistanceKm;
    }

    public void setMissDistanceKm(double missDistanceKm) {
        this.missDistanceKm = missDistanceKm;
    }

    public double getRelativeVelocityKph() {
        return relativeVelocityKph;
    }

    public void setRelativeVelocityKph(double relativeVelocityKph) {
        this.relativeVelocityKph = relativeVelocityKph;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public void setOrbitingBody(String orbitingBody) {
        this.orbitingBody = orbitingBody;
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }

    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }
}
