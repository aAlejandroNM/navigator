package com.solvd.navigator.cli.menus;

import com.solvd.navigator.controller.EdgeController;
import com.solvd.navigator.controller.RouteController;
import com.solvd.navigator.controller.LocationController;
import com.solvd.navigator.controller.NavigationController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MainMenu {

    private static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

    private final NavigationController navigationController;
    private final LocationController locationController;
    private final RouteController routeController;
    private final EdgeController edgeController;
    private final Scanner scanner;

    public MainMenu(NavigationController navigationController,
                    LocationController locationController,
                    EdgeController edgeController,
                    RouteController routeController,
                    Scanner scanner) {
        this.navigationController = navigationController;
        this.locationController = locationController;
        this.edgeController = edgeController;
        this.routeController = routeController;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;

        while (running) {
            LOGGER.info("=== NAVIGATOR MENU ===");
            LOGGER.info("1. Location options");
            LOGGER.info("2. Edge options");
            LOGGER.info("3. Route options");
            LOGGER.info("4. Find shortest route");
            LOGGER.info("Q. Quit");
            LOGGER.info("Choose an option:");

            String option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    new LocationMenu(locationController, scanner).start();
                    break;

                case "2":
                    new EdgeMenu(edgeController, scanner).start();
                    break;

                case "3":
                    new RouteMenu(routeController, scanner).start();
                    break;

                case "4":
                    runFindShortestRoute();
                    break;

                case "Q":
                    running = false;
                    break;

                default:
                    LOGGER.warn("Invalid option, please try again.");
            }
        }

        LOGGER.info("Exiting Navigator...");
    }

    private void runFindShortestRoute() {
        LOGGER.info("Enter source location name: ");
        String source = scanner.nextLine().trim();

        LOGGER.info("Enter target location name: ");
        String target = scanner.nextLine().trim();

        try {
            var result = navigationController.findPath(source, target);

            if (result == null || !result.pathExists()) {
                LOGGER.warn("No route found between {} and {}.", source, target);
                return;
            }

            LOGGER.info("\n\n=== Shortest Path Result ===\n{}\n", result.toString());
        } catch (Exception e) {
            LOGGER.error("Error calculating shortest route: {}", e.getMessage());
        }
    }
}
