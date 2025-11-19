package com.solvd.navigator.cli.commands;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.cli.util.PaginationUtil;
import com.solvd.navigator.controller.NavigationController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListLocationsCommand implements Command {

    private final NavigationController controller;
    private static final Logger LOGGER = LogManager.getLogger(ListLocationsCommand.class);

    public ListLocationsCommand(NavigationController controller) {
        this.controller = controller;
    }

    @Override
    public String name() { return "List all locations"; }

    @Override
    public String key() { return "1"; }

    @Override
    public void execute() {
        List<Location> locations = controller.getAllLocations();
        PaginationUtil.paginateLocations(locations);
    }
}

