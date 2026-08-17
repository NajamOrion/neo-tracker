# NEO Tracker

[![CI](https://github.com/NajamOrion/neo-tracker/actions/workflows/ci.yml/badge.svg)](https://github.com/NajamOrion/neo-tracker/actions/workflows/ci.yml)

This is a Spring Boot service that pulls near-Earth asteroid data from NASA, classifies and stores it.
The interesting part is that it detects changes in an asteroid's data and scores how much you would trust each record.

Most asteroid trackers fetch NASA's feed and show it, overwriting whatever they had before.
I wanted to build something that treats the upstream data the way I learned to treat it doing integration work: as something that gets corrected over time, and shouldn't be trusted blindly if we have no way
to see what changed and why.
So this one keeps history, spots the changes, and puts a confidence rating on every asteroid, with reasons behind it.


## What it does

- **Fetches and stores** asteroids approaching Earth over a date range, pulled from NASA's NeoWs feed and deduplicated into a local PostgreSQL database.
- **Classifies** each one into a size class (SMALL/MEDIUM/LARGE) that the NASA API doesn't provide.
- **Audits data quality**: detects when NASA revises an asteroid's diameter, hazard status, or miss distance between fetches, logs every change with its history, and computes an explainable confidence rating.
- **Handles failure gracefully**: clean, consistent error responses, including when the NASA API itself is unavailable.

There is also a small mission terminal-styled frontend (served from the app) for fetching, filtering, and inspecting an asteroid's data-quality report.

![NEO Tracker demo](assets/docs/neo_tracker_demo.gif)
Demo: Fetching asteroids, filtering the list, and inspecting an asteroid's confidence rating and change history

---

## Tech stack
Java 21 - Spring Boot 3.5 (Web, Data JPA, Validation) - PostgreSQL (H2 in-memory for tests) - JPA / Hibernate - Spring RestClient - JUnit 5 + Mockito - Maven - Github Actions

---

## How it's built

It's a layered Spring Boot app where the controllers handle HTTP, services hold the logic, repositories talk to the database.
One of the design decisions is based on the fact that NASA's JSON is nested, comes in several unit systems and quotes its numbers as strings.
Instead of letting this shape into my database, I caught it in dedicated mapping classes, translated it into an entity and then exposed a response to clients:

```
NASA JSON  ->  dto/nasa/*  ->  Asteroid entity  ->  response DTO  -> API client  
(External)     (Catch it)          (store)           (expose it)  
```

If NASA changes its response format, the only thing that breaks is my mapping layer. My database schema and public API remain as is.

---

## Running locally

**You will need:** Java 21, PostgreSQL running locally and a free NASA API key from [api.nasa.gov](https://api.nasa.gov/).

1. Create the database:
   ```
   sql
      CREATE DATABASE neo_tracker_db;
   ```

2. Set two environment variables (no credentials are committed and the properties file just references these):
   ```
      DB_PASSWORD=your_postgres_password
      NASA_API_KEY=your_nasa_api_key
   ```

3. Run it:
   ```
      ./mvnw spring-boot:run
   ```

   Since this is a local project for now, it starts on `http://localhost:8080`. The frontend is at `http://localhost:8080/neo-tracker-terminal.html`.

   **Tests:**
   ```
   ./mvnw test
   ```
   They run against an in-memory H2 database and need no setup or credentials.

   ---

   ## API

   **Fetch from NASA and store** *(max 7-day range)*
   ```
   POST /asteroids/fetch?startDate=2026-08-08&endDate=2026-08-08
   ```

   **Query what's stored**
   ```
   GET /asteroids                     # all asteroids
   GET /asteroids/{id}                # one asteroid 
   GET /asteroids/hazardous           # hazardous asteroids
   DELETE /asteroids/{id}             # remove an asteroid
   ```

   **Data quality**
   ```
   GET /asteroids/{id}/history               # change history for one asteroid
   GET /asteroids/changed?since=2026-08-01   # get all changes logged since a specific date
   GET /asteroids/{id}/quality               # confidence rating and reasons
   GET /asteroids/low-confidence             # asteroids rated as LOW confidence
   ```

   ---

   ## The decisions behind it

   **Keeping NASA's shape out of the app's domain:** As mentioned above, this helps in setting up what is sometimes known as an "anti-corruption layer".

   **Size Classification:** I classify on the midpoint diameter: SMALL under 50m, MEDIUM 50-139m, LARGE 140m and up. The 140m mark is not arbitrary as it is NASA's own threshold for a potentially-hazardous asteroid
   and this is where impacts become devastating regionally. The 50m mark is my own call and is based on the Tunguska event which involved an object of approximately 50m that was still dangerous in terms of damage caused.
   One boundary is a real stand while the other is my convention.

   **Confidence scoring:** it is a heuristic and not science. Every asteroid get a HIGH / MEDIUM / LOW rating from a transparent deduction model: it start at 100 and subtracts points for real signals such as
   missing close-approach data, an invalid diameter, data that's been revised repeatedly, and hazard reclassification. The '/quality' endpoint returns the *reasons* so that the rating can always explain itself.
   I'm clear on the fact that this is my judgment and not a scientific measurement. The signals are real, the weights are mine, and I'd tune them with more/updated data.

   One thing that actually happened while building this: my first version of the confidence model also had a "diameter uncertainty" signal. When I ran it on real data, it flagged *every* asteroid. To check what was wrong,
   I logged the actual spreads and found they were mathematically identical across all of them. The signal carried no information, so I removed it.

   **Change detection with history:** On each re-fetch, incoming data is compared against the last stored snapshot before anything is overwritten. Real differences get logged; identical data logs nothing (no false positives).
   This only works because the app keeps point-in-time snapshots, which is also what makes the confidence model's "has this been revised" signal possible.

   **Failing fast:** The 7-day range check runs before any NASA call.

   **Secrets and credentials stay out of sight:** The database password and NASA API key are both environment variables and nothing is exposed to the public.

   ---

   ## Testing

   The testing suite covers the happy path, the error paths and the boundaries.

   ---

   ## Potential additions as this is still an ongoing project

   - Scheduled re-fetching so change detection runs on its own instead of on demand.
   - Notifications when a tracked asteroid's status changes.
   - User accounts
   - Time-weighting the confidence model so a revision from last week counts as more that one from a year ago.
   - An improved frontend interface with more features.
   - Public deployment
  
   ---

   ## A note on how I built this

   Part of the aim of this project was to try AI-powered development for myself and see how it fits into a real build. I used AI assistance for planning and in-editor completion.
   Several times I pushed back or corrected course (some examples): I caught a Spring Boot dependency-version
   mismatch the assistant diagnosed; I removed a confidence signal after logging real data showed it was noise; I flagged a change-log response that didn't identify which asteroid
   had changed; and I questioned design choices like the size-class thresholds and whether the confidence score was overclaiming. The tooling made me faster but I emphasized understanding
   and questioning each decisions, rather than just accepting it.

   ---

   ## Data

   Asteroid data comes from [NASA's NeoWs (Near Earth Object Web Service)](https://api.nasa.gov/), which is public domain. The size class and confidence rating are my own derived layers, not NASA classifications.









