package com.solvd.navigator;

import com.solvd.navigator.cli.menus.MainMenu;
import com.solvd.navigator.controller.EdgeController;
import com.solvd.navigator.controller.LocationController;
import com.solvd.navigator.controller.NavigationController;

import com.solvd.navigator.controller.RouteController;
import com.solvd.navigator.dao.mysql.impl.EdgeDao;
import com.solvd.navigator.dao.mysql.impl.LocationDao;
import com.solvd.navigator.dao.mysql.impl.RouteDao;
import com.solvd.navigator.dao.mysql.impl.RouteLocationDao;

import com.solvd.navigator.dao.mysql.interfaces.*;

import com.solvd.navigator.service.EdgeService;
import com.solvd.navigator.service.LocationService;
import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("Starting Navigator Application...");

        // -----------------------------
        // 1. DAO LAYER
        // -----------------------------
        ILocationDao locationDao = new LocationDao();
        IEdgeDao edgeDao = new EdgeDao(locationDao);
        IRouteLocationDao routeLocationDao = new RouteLocationDao(locationDao);
        IRouteDao routeDao = new RouteDao(routeLocationDao);

        // -----------------------------
        // 2. SERVICE LAYER
        // -----------------------------
        LocationService locationService = new LocationService(locationDao, edgeDao, routeLocationDao);
        EdgeService edgeService = new EdgeService(edgeDao, locationDao);

        NavigationService navigationService = NavigationService.fromDaos(
                locationDao,
                edgeDao,
                routeDao,
                routeLocationDao
        );

        // -----------------------------
        // 3. CONTROLLER
        // -----------------------------
        NavigationController navigationController =
                new NavigationController(navigationService);
        LocationController locationController = new LocationController(locationService);
        EdgeController edgeController = new EdgeController(edgeService);
        RouteController routeController = new RouteController(routeDao, locationDao, routeLocationDao);

        // -----------------------------
        // 4. MENUS
        // -----------------------------
        Scanner scanner = new Scanner(System.in);

        MainMenu mainMenu = new MainMenu(navigationController, locationController, edgeController, routeController, scanner);
        mainMenu.start();

        LOGGER.info("Navigator Application closed.");
    }
}
