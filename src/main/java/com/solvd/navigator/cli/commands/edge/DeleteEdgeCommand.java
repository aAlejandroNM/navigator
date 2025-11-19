package com.solvd.navigator.cli.commands.edge;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.controller.EdgeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DeleteEdgeCommand implements Command {

    private final EdgeController controller;
    private final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(DeleteEdgeCommand.class);

    public DeleteEdgeCommand(EdgeController controller) {
        this.controller = controller;
    }

    @Override
    public String name() { return "Delete an edge"; }

    @Override
    public String key() { return "4"; }

    @Override
    public void execute() {
        try {
            LOGGER.info("Enter Edge ID to delete: ");
            Long id = Long.valueOf(scanner.nextLine().trim());

            boolean ok = controller.deleteEdge(id);
            LOGGER.info(ok ? "Edge deleted." : "Edge not found.");
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid number. Aborting.");
        } catch (Exception e) {
            LOGGER.error("Error deleting edge: {}", e.getMessage());
        }
    }
}