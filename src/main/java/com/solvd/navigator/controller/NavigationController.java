package com.solvd.navigator.controller;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.NavigationService;

import java.util.List;

public class NavigationController {

    private final NavigationService navigationService;

    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public PathResult findPath(String sourceLocationName, String targetLocationName) {
        if (sourceLocationName == null || sourceLocationName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación origen no puede estar vacío.");
        }

        if (targetLocationName == null || targetLocationName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación destino no puede estar vacío.");
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