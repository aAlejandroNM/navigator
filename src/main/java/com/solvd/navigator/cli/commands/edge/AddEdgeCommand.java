package com.solvd.navigator.cli.commands.edge;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.controller.EdgeController;
import com.solvd.navigator.model.Edge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class AddEdgeCommand implements Command {

    private final EdgeController controller;
    private final Scanner scanner;
    private static final Logger LOGGER = LogManager.getLogger(AddEdgeCommand.class);

    public AddEdgeCommand(EdgeController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public String name() { return "Add a new edge"; }

    @Override
    public String key() { return "2"; }

    @Override
    public void execute() {
        try {
            LOGGER.info("From Location ID: ");
            Long fromId = Long.valueOf(scanner.nextLine().trim());

            LOGGER.info("To Location ID: ");
            Long toId = Long.valueOf(scanner.nextLine().trim());

            LOGGER.info("Weight (distance): ");
            Double weight = Double.valueOf(scanner.nextLine().trim());

            LOGGER.info("Directed? (true/false): ");
            Boolean directed = Boolean.valueOf(scanner.nextLine().trim());

            LOGGER.info("Name (optional): ");
            String name = scanner.nextLine().trim();

            LOGGER.info("Active? (true/false): ");
            Boolean active = Boolean.valueOf(scanner.nextLine().trim());

            Edge created = controller.createEdge(fromId, toId, weight, directed, name.isEmpty() ? null : name, active);
            if (created != null) {
                LOGGER.info("Edge created with ID: {}", created.getId());
            } else {
                LOGGER.warn("Edge could not be created.");
            }
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid numeric input. Aborting.");
        } catch (Exception e) {
            LOGGER.error("Error creating edge: {}", e.getMessage());
        }
    }
}