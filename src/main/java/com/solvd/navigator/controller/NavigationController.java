package com.solvd.navigator.controller;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NavigationController {

    private final NavigationService navigationService;
    private static final Logger LOGGER = LogManager.getLogger(NavigationController.class);

    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public PathResult findPath(String sourceLocationName, String targetLocationName) {
        LOGGER.info("Received request to find shortest path: '{}' -> '{}'",
                sourceLocationName, targetLocationName);

        if (sourceLocationName == null || sourceLocationName.trim().isEmpty()) {
            throw new IllegalArgumentException("The source location name cannot be empty.");
        }

        if (targetLocationName == null || targetLocationName.trim().isEmpty()) {
            throw new IllegalArgumentException("The destination location name cannot be empty.");
        }

        return navigationService.findShortestPath(
                sourceLocationName.trim(),
                targetLocationName.trim()
        );
    }

    public List<Location> getAllLocations() {
        return navigationService.getAllLocations();
    }


    public boolean locationExists(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return false;
        }

        List<Location> locations = navigationService.getAllLocations();
        return locations.stream()
                .anyMatch(location -> locationName.equalsIgnoreCase(location.getName()));
    }
}