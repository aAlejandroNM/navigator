package com.solvd.navigator.cli.menus;

import com.solvd.navigator.controller.NavigationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MainMenu {

    private static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

    private final NavigationController controller;
    private final Scanner scanner;

    public MainMenu(NavigationController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;

        while (running) {
            LOGGER.info("\n=== NAVIGATOR MENU ===");
            LOGGER.info("1. Location options");
            LOGGER.info("2. Find shortest route");
            LOGGER.info("Q. Quit");
            LOGGER.info("Choose an option: ");

            String option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    new LocationMenu(controller, scanner).start();
                    break;

                case "2":
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
            var result = controller.findPath(source, target);

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
