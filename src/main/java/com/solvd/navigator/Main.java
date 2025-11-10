package com.solvd.navigator;

import com.solvd.navigator.dao.impl.mysql.LocationDAO;
import com.solvd.navigator.model.Location;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("=== Navigator DAO Test Started ===");

        LocationDAO locationDAO = new LocationDAO();

        Location location = new Location.Builder()
                .withName("East Park")
                .withX(60.785091)
                .withY(-103.968285)
                .withType("Park")
                .withDescription("A large public park in New York City at East")
                .build();

        LOGGER.info("--- Saving new Location ---");
        Location saved = locationDAO.save(location);
        if (saved != null) {
            LOGGER.info("Location saved successfully with ID: {}", saved.getId());
        } else {
            LOGGER.error("Failed to save the location.");
        }

        if (saved != null) {
            Optional<Location> fetched = locationDAO.findById(saved.getId());
            LOGGER.info("--- Fetch by ID ---");
            fetched.ifPresentOrElse(
                    loc -> LOGGER.info("Found: {} (ID: {})", loc.getName(), loc.getId()),
                    () -> LOGGER.warn("Location with ID {} not found", saved.getId())
            );
        }

        LOGGER.info("--- All Locations ---");
        List<Location> all = locationDAO.findAll();
        if (all.isEmpty()) {
            LOGGER.warn("No locations found in database.");
        } else {
            all.forEach(loc -> LOGGER.info("{}: {}", loc.getId(), loc.getName()));
        }

        LOGGER.info("--- Exists by name ---");
        boolean exists = locationDAO.existsByName("Central Park");
        LOGGER.info("Exists 'Central Park'? {}", exists);

        if (saved != null) {
            Location updated = new Location.Builder()
                    .withId(saved.getId())
                    .withName("Central Park NYC")
                    .withX(saved.getX())
                    .withY(saved.getY())
                    .withType(saved.getType())
                    .withDescription("Updated description")
                    .build();

            LOGGER.info("--- Updating Location ---");
            locationDAO.update(updated);
            LOGGER.info("Location with ID {} updated successfully.", updated.getId());
        }

        if (saved != null) {
            LOGGER.info("--- Deleting Location ---");
            locationDAO.delete(saved.getId());
            LOGGER.info("Deleted Location with ID: {}", saved.getId());
        }

        LOGGER.info("=== Navigator DAO Test Finished ===");
    }
}