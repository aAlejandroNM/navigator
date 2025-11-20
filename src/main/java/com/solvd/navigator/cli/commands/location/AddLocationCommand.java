package com.solvd.navigator.cli.commands.location;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.controller.LocationController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class AddLocationCommand implements Command {

    private final LocationController locationController;
    private final Scanner scanner;
    private static final Logger LOGGER = LogManager.getLogger(AddLocationCommand.class);

    public AddLocationCommand(LocationController locationController, Scanner scanner) {
        this.locationController = locationController;
        this.scanner = scanner;
    }

    @Override
    public String name() { return "Add a new location"; }

    @Override
    public String key() { return "2"; }

    @Override
    public void execute() {
        LOGGER.info("Name: ");
        String name = scanner.nextLine();

        LOGGER.info("Description: ");
        String desc = scanner.nextLine();

        LOGGER.info("X coordinate: ");
        Double x = Double.valueOf(scanner.nextLine());

        LOGGER.info("Y coordinate: ");
        Double y = Double.valueOf(scanner.nextLine());

        LOGGER.info("Type (CITY, AIRPORT, etc.): ");
        String type = scanner.nextLine();

        Location created = locationController.addLocation(name, desc, x, y, type);
        LOGGER.info("Location added with ID: {}", created.getId());
    }
}
