package com.solvd.navigator.cli.commands;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.service.LocationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class UpdateLocationCommand implements Command {

    private final LocationService locationService;
    private final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(UpdateLocationCommand.class);

    public UpdateLocationCommand(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public String name() {
        return "Update a location";
    }

    @Override
    public String key() {
        return "3";
    }

    @Override
    public void execute() {
        LOGGER.info("Enter Location ID: ");
        Long id = Long.valueOf(scanner.nextLine());

        LOGGER.info("New name: ");
        String name = scanner.nextLine();

        LOGGER.info("New description: ");
        String desc = scanner.nextLine();

        LOGGER.info("New X: ");
        Double x = Double.valueOf(scanner.nextLine());

        LOGGER.info("New Y: ");
        Double y = Double.valueOf(scanner.nextLine());

        LOGGER.info("New type: ");
        String type = scanner.nextLine();

        boolean ok = locationService.updateLocation(id, name, desc, x, y, type);

        LOGGER.info(ok ? "Updated successfully." : "Location not found.");
    }
}
