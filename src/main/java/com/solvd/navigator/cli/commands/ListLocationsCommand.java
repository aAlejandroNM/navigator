package com.solvd.navigator.cli.commands;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.service.LocationService;
import com.solvd.navigator.cli.util.PaginationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListLocationsCommand implements Command {

    private final LocationService locationService;
    private static final Logger LOGGER = LogManager.getLogger(ListLocationsCommand.class);

    public ListLocationsCommand(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public String name() {
        return "List all locations";
    }

    @Override
    public String key() {
        return "1";
    }

    @Override
    public void execute() {
        List<Location> locations = locationService.listLocations();
        PaginationUtil.paginateLocations(locations);
    }
}
