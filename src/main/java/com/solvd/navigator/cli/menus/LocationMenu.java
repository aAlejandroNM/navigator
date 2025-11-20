package com.solvd.navigator.cli.menus;

import com.solvd.navigator.cli.CommandRegistry;
import com.solvd.navigator.controller.LocationController;
import com.solvd.navigator.cli.commands.location.AddLocationCommand;
import com.solvd.navigator.cli.commands.location.DeleteLocationCommand;
import com.solvd.navigator.cli.commands.location.ListLocationsCommand;
import com.solvd.navigator.cli.commands.location.UpdateLocationCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class LocationMenu {

    private static final Logger LOGGER = LogManager.getLogger(LocationMenu.class);

    private final CommandRegistry registry = new CommandRegistry();
    private final Scanner scanner;

    public LocationMenu(LocationController locationController, Scanner scanner) {
        this.scanner = scanner;

        registry.register(new ListLocationsCommand(locationController));
        registry.register(new AddLocationCommand(locationController, scanner));
        registry.register(new UpdateLocationCommand(locationController, scanner));
        registry.register(new DeleteLocationCommand(locationController, scanner));
    }

    public void start() {
        while (true) {
            LOGGER.info("=== LOCATION OPTIONS ===");
            LOGGER.info("1. List all locations");
            LOGGER.info("2. Add location");
            LOGGER.info("3. Update location");
            LOGGER.info("4. Delete location");
            LOGGER.info("B. Back");
            LOGGER.info("Choose an option: ");

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("B")) {
                LOGGER.info("Returning to main menu...");
                return;
            }

            var cmd = registry.get(input);
            if (cmd != null) {
                cmd.execute();
            } else {
                LOGGER.warn("Invalid option. Try again.");
            }
        }
    }
}
