package com.solvd.navigator.controller;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class CliController {

    private static final Logger LOGGER = LogManager.getLogger(CliController.class);

    private final NavigationService navigationService;
    private final Scanner scanner;

    public CliController(NavigationService navigationService) {
        this.navigationService = navigationService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> listLocations();
                case "2" -> findShortestPath();
                case "3" -> running = false;
                default -> LOGGER.warn("Invalid option, try again.");
            }
        }

        LOGGER.info("Exiting Navigator App. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n=== NAVIGATOR MENU ===");
        System.out.println("1. List all locations");
        System.out.println("2. Find shortest path");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private void listLocations() {
        List<Location> locations = navigationService.getAllLocations();
        System.out.println("\n--- Locations ---");
        locations.forEach(loc -> System.out.println("- " + loc.getName()));
    }

    private void findShortestPath() {
        System.out.print("Enter source location: ");
        String source = scanner.nextLine();

        System.out.print("Enter target location: ");
        String target = scanner.nextLine();

        try {
            PathResult result = navigationService.findShortestPath(source, target);
            System.out.println("\n--- Result ---");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            LOGGER.error("Path error", e);
        }
    }
}
