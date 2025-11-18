package com.solvd.navigator.controller;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.LocationService;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NavigationController {

    private final LocationService locationService;
    private final NavigationService navigationService;
    private static final Logger LOGGER = LogManager.getLogger(NavigationController.class);

    public NavigationController(LocationService locationService,
                                NavigationService navigationService) {
        this.locationService = locationService;
        this.navigationService = navigationService;
    }

    public Location addLocation(String name,
                                String description,
                                Double x,
                                Double y,
                                String type) {
        return locationService.addLocation(name, description, x, y, type);
    }

    public boolean updateLocation(Long id,
                                  String newName,
                                  String newDescription,
                                  Double newX,
                                  Double newY,
                                  String newType) {
        return locationService.updateLocation(id, newName, newDescription, newX, newY, newType);
    }

    public boolean deleteLocation(Long id) {
        return locationService.deleteLocation(id);
    }

    public List<Location> getAllLocations() {
        return locationService.listLocations();
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

    public boolean locationExists(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return false;
        }

        List<Location> locations = locationService.listLocations();
        return locations.stream()
                .anyMatch(location -> locationName.equalsIgnoreCase(location.getName()));
    }
}