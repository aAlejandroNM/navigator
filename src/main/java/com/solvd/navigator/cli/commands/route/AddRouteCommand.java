package com.solvd.navigator.cli.commands.route;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.controller.RouteController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddRouteCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddRouteCommand.class);
    private final RouteController controller;
    private final Scanner scanner;

    public AddRouteCommand(RouteController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public String name() {
        return "Add new route";
    }

    @Override
    public String key() {
        return "2";
    }

    @Override
    public void execute() {
        LOGGER.info("Enter route name: ");
        String name = scanner.nextLine();

        LOGGER.info("Enter route description: ");
        String desc = scanner.nextLine();

        List<Location> locations = new ArrayList<>();
        while (true) {
            LOGGER.info("Enter location ID to add to route (or B to finish): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("B")) break;

            try {
                Long id = Long.parseLong(input);
                controller.findLocationById(id).ifPresentOrElse(
                        locations::add,
                        () -> LOGGER.warn("Location with ID {} not found.", id)
                );
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid ID format.");
            }
        }

        Route route = controller.addRoute(name, desc, locations);
        LOGGER.info("Route '{}' created with {} locations.", route.getName(),
                route.getLocations() != null ? route.getLocations().size() : 0);
    }
}