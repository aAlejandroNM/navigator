package com.solvd.navigator.cli.commands.edge;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.controller.EdgeController;
import com.solvd.navigator.model.Edge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListEdgesCommand implements Command {

    private final EdgeController controller;
    private static final Logger LOGGER = LogManager.getLogger(ListEdgesCommand.class);

    public ListEdgesCommand(EdgeController controller) {
        this.controller = controller;
    }

    @Override
    public String name() { return "List all edges"; }

    @Override
    public String key() { return "1"; }

    @Override
    public void execute() {
        List<Edge> edges = controller.getAllEdges();
        if (edges == null || edges.isEmpty()) {
            LOGGER.info("No edges found.");
            return;
        }
        LOGGER.info("=== Edges ===");
        for (Edge e : edges) {
            LOGGER.info("id={} | {} -> {} | w={} | dir={} | name={} | active={}",
                    e.getId(),
                    e.getFrom() != null ? e.getFrom().getName() : "null",
                    e.getTo() != null ? e.getTo().getName() : "null",
                    e.getWeight(),
                    e.isDirected(),
                    e.getName(),
                    e.isActive());
        }
    }
}