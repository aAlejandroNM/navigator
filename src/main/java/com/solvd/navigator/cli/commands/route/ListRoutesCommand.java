package com.solvd.navigator.cli.commands.route;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.controller.RouteController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListRoutesCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ListRoutesCommand.class);
    private final RouteController controller;

    public ListRoutesCommand(RouteController controller) {
        this.controller = controller;
    }

    @Override
    public String name() {
        return "List all routes";
    }

    @Override
    public String key() {
        return "1";
    }

    @Override
    public void execute() {
        List<Route> routes = controller.getAllRoutes();
        if (routes.isEmpty()) {
            LOGGER.info("No routes available.");
        } else {
            for (Route r : routes) {
                LOGGER.info("ID: {}, Name: {}, Description: {}, Locations count: {}",
                        r.getId(), r.getName(), r.getDescription(),
                        r.getLocations() != null ? r.getLocations().size() : 0);
            }
        }
    }
}