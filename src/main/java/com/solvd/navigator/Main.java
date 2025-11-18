package com.solvd.navigator;

import com.solvd.navigator.cli.CliController;
import com.solvd.navigator.cli.CommandRegistry;

import com.solvd.navigator.cli.commands.*;

import com.solvd.navigator.dao.mysql.impl.EdgeDao;
import com.solvd.navigator.dao.mysql.impl.LocationDao;
import com.solvd.navigator.dao.mysql.impl.RouteDao;
import com.solvd.navigator.dao.mysql.impl.RouteLocationDao;

import com.solvd.navigator.dao.mysql.interfaces.*;

import com.solvd.navigator.service.LocationService;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("Starting Navigator Application...");

        // -----------------------------
        // 1. DAO LAYER
        // -----------------------------
        IRouteDao routeDao = new RouteDao();
        ILocationDao locationDao = new LocationDao();
        IEdgeDao edgeDao = new EdgeDao(locationDao);
        IRouteLocationDao routeLocationDao = new RouteLocationDao(locationDao);

        // -----------------------------
        // 2. SERVICE LAYER
        // -----------------------------
        LocationService locationService = new LocationService(locationDao, edgeDao, routeLocationDao);

        NavigationService navigationService = NavigationService.fromDaos(
                locationDao,
                edgeDao,
                routeDao,
                routeLocationDao
        );

        // -----------------------------
        // 3. COMMAND REGISTRY
        // -----------------------------
        CommandRegistry registry = new CommandRegistry();

        registry.register(new AddLocationCommand(locationService));
        registry.register(new DeleteLocationCommand(locationService));
        registry.register(new UpdateLocationCommand(locationService));
        registry.register(new ListLocationsCommand(locationService));
        registry.register(new FindRouteCommand(navigationService));

        LOGGER.info("Registered {} CLI commands.", registry.getAll().size());

        // -----------------------------
        // 4. START CLI
        // -----------------------------
        CliController cli = new CliController(registry);
        cli.start();
    }
}
