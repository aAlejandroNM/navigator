package com.solvd.navigator.cli.commands.location;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.cli.util.PaginationUtil;
import com.solvd.navigator.controller.NavigationController;

import java.util.List;

public class ListLocationsCommand implements Command {

    private final NavigationController controller;

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

