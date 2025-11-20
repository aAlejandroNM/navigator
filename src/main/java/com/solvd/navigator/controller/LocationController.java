package com.solvd.navigator.controller;

import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.LocationService;

import java.util.List;

public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    public Location addLocation(String name, String description, Double x, Double y, String type) {
        return locationService.addLocation(name, description, x, y, type);
    }

    public boolean updateLocation(Long id, String newName, String newDescription, Double newX, Double newY, String newType) {
        return locationService.updateLocation(id, newName, newDescription, newX, newY, newType);
    }

    public boolean deleteLocation(Long id) {
        return locationService.deleteLocation(id);
    }

    public List<Location> getAllLocations() {
        return locationService.listLocations();
    }

    public boolean locationExists(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return false;
        }

        List<Location> locations = locationService.listLocations();
        return locations.stream().anyMatch(location -> locationName.equalsIgnoreCase(location.getName()));
    }
}