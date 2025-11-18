package com.solvd.navigator.cli.commands;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.service.LocationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DeleteLocationCommand implements Command {

    private final LocationService locationService;
    private final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(DeleteLocationCommand.class);

    public DeleteLocationCommand(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public String name() {
        return "Delete a location";
    }

    @Override
    public String key() {
        return "4";
    }

    @Override
    public void execute() {
        LOGGER.info("Enter Location ID: ");
        Long id = Long.valueOf(scanner.nextLine());

        try {
            boolean ok = locationService.deleteLocation(id);
            LOGGER.info(ok ? "Deleted successfully." : "Location not found.");
        } catch (Exception e) {
            LOGGER.info("Cannot delete: {}", e.getMessage());
        }
    }
}