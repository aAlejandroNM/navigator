package com.solvd.navigator.cli.commands.edge;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.controller.EdgeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class UpdateEdgeCommand implements Command {

    private final EdgeController controller;
    private final Scanner scanner;
    private static final Logger LOGGER = LogManager.getLogger(UpdateEdgeCommand.class);

    public UpdateEdgeCommand(EdgeController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public String name() { return "Update an edge"; }

    @Override
    public String key() { return "3"; }

    @Override
    public void execute() {
        try {
            LOGGER.info("Edge ID to update: ");
            Long id = Long.valueOf(scanner.nextLine().trim());

            LOGGER.info("New From Location ID (or blank to keep): ");
            String sFrom = scanner.nextLine().trim();
            Long newFrom = sFrom.isEmpty() ? null : Long.valueOf(sFrom);

            LOGGER.info("New To Location ID (or blank to keep): ");
            String sTo = scanner.nextLine().trim();
            Long newTo = sTo.isEmpty() ? null : Long.valueOf(sTo);

            LOGGER.info("New weight (or blank to keep): ");
            String sW = scanner.nextLine().trim();
            Double newWeight = sW.isEmpty() ? null : Double.valueOf(sW);

            LOGGER.info("Directed? (true/false or blank to keep): ");
            String sDir = scanner.nextLine().trim();
            Boolean newDirected = sDir.isEmpty() ? null : Boolean.valueOf(sDir);

            LOGGER.info("New name (or blank to keep): ");
            String newName = scanner.nextLine().trim();
            newName = newName.isEmpty() ? null : newName;

            LOGGER.info("Active? (true/false or blank to keep): ");
            String sAct = scanner.nextLine().trim();
            Boolean newActive = sAct.isEmpty() ? null : Boolean.valueOf(sAct);

            boolean ok = controller.updateEdge(id, newFrom, newTo, newWeight, newDirected, newName, newActive);
            LOGGER.info(ok ? "Edge updated successfully." : "Edge not found.");
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid numeric input. Aborting.");
        } catch (Exception e) {
            LOGGER.error("Error updating edge: {}", e.getMessage());
        }
    }
}