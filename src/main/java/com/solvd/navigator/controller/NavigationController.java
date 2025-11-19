package com.solvd.navigator.controller;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NavigationController {

    private static final Logger LOGGER = LogManager.getLogger(NavigationController.class);

    private final NavigationService navigationService;

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
}
