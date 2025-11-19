package com.solvd.navigator.cli.menus;

import com.solvd.navigator.cli.CommandRegistry;
import com.solvd.navigator.controller.RouteController;
import com.solvd.navigator.cli.commands.route.AddRouteCommand;
import com.solvd.navigator.cli.commands.route.DeleteRouteCommand;
import com.solvd.navigator.cli.commands.route.ListRoutesCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class RouteMenu {

    private static final Logger LOGGER = LogManager.getLogger(RouteMenu.class);
    private final CommandRegistry registry = new CommandRegistry();
    private final Scanner scanner;

    public RouteMenu(RouteController controller, Scanner scanner) {
        this.scanner = scanner;

        registry.register(new ListRoutesCommand(controller));
        registry.register(new AddRouteCommand(controller, scanner));
        registry.register(new DeleteRouteCommand(controller, scanner));
    }

    public void start() {
        while (true) {
            LOGGER.info("=== ROUTE OPTIONS ===");
            LOGGER.info("1. List all routes");
            LOGGER.info("2. Add a new route");
            LOGGER.info("3. Delete a route");
            LOGGER.info("B. Back");
            LOGGER.info("Choose an option: ");

            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("B")) return;

            var cmd = registry.get(input);
            if (cmd != null) {
                cmd.execute();
            } else {
                LOGGER.warn("Invalid option. Try again.");
            }
        }
    }
}