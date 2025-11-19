package com.solvd.navigator.cli.menus;

import com.solvd.navigator.cli.CommandRegistry;
import com.solvd.navigator.controller.EdgeController;
import com.solvd.navigator.cli.commands.edge.AddEdgeCommand;
import com.solvd.navigator.cli.commands.edge.DeleteEdgeCommand;
import com.solvd.navigator.cli.commands.edge.ListEdgesCommand;
import com.solvd.navigator.cli.commands.edge.UpdateEdgeCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class EdgeMenu {

    private static final Logger LOGGER = LogManager.getLogger(EdgeMenu.class);

    private final CommandRegistry registry = new CommandRegistry();
    private final Scanner scanner;

    public EdgeMenu(EdgeController controller, Scanner scanner) {
        this.scanner = scanner;

        registry.register(new ListEdgesCommand(controller));
        registry.register(new AddEdgeCommand(controller));
        registry.register(new UpdateEdgeCommand(controller));
        registry.register(new DeleteEdgeCommand(controller));
    }

    public void start() {
        while (true) {
            LOGGER.info("=== EDGE OPTIONS ===");
            LOGGER.info("1. List all edges");
            LOGGER.info("2. Add edge");
            LOGGER.info("3. Update edge");
            LOGGER.info("4. Delete edge");
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