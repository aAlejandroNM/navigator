package com.solvd.navigator.cli.commands.route;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.controller.RouteController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DeleteRouteCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(DeleteRouteCommand.class);
    private final RouteController controller;
    private final Scanner scanner;

    public DeleteRouteCommand(RouteController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public String name() {
        return "Delete a route";
    }

    @Override
    public String key() {
        return "3";
    }

    @Override
    public void execute() {
        LOGGER.info("Enter route ID to delete: ");
        String input = scanner.nextLine().trim();
        try {
            Long id = Long.parseLong(input);
            boolean ok = controller.deleteRoute(id);
            LOGGER.info(ok ? "Route deleted successfully." : "Route not found.");
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid ID format.");
        }
    }
}