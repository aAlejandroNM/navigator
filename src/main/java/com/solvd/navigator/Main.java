package com.solvd.navigator;

import com.solvd.navigator.dao.mysql.impl.EdgeDao;
import com.solvd.navigator.dao.mysql.impl.RouteDao;
import com.solvd.navigator.dao.mysql.impl.LocationDao;
import com.solvd.navigator.dao.mysql.impl.RouteLocationDao;

import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.controller.NavigationController;

import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        // -----------------------------
        // 1. Initialize DAO Layer
        // -----------------------------
        ILocationDao locationDao = new LocationDao();
        IEdgeDao edgeDao = new EdgeDao(locationDao);
        IRouteDao routeDao = new RouteDao();
        IRouteLocationDao routeLocationDao = new RouteLocationDao(locationDao);

        // -----------------------------
        // 2. Initialize Service Layer
        // -----------------------------
        NavigationService navigationService =
                NavigationService.fromDaos(locationDao, edgeDao, routeDao, routeLocationDao);

        // -----------------------------
        // 3. Initialize Controller
        // -----------------------------
        NavigationController controller = new NavigationController(navigationService);

        // -----------------------------
        // 4. CLI user input
        // -----------------------------
        Scanner scanner = new Scanner(System.in);

        LOGGER.info("=== NAVIGATOR SYSTEM ===");
        LOGGER.debug("All locations loaded:");
        controller.getAllLocations()
                .forEach(loc -> LOGGER.info("- " + loc.getName()));

        LOGGER.info("\nEnter source location: ");
        String source = scanner.nextLine();

        LOGGER.info("Enter target location: ");
        String target = scanner.nextLine();

        // -----------------------------
        // 5. Call Controller
        // -----------------------------
        try {
            PathResult result = controller.findPath(source, target);
            LOGGER.info("\n=== RESULT ===");
            LOGGER.info(result.toString());

        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }

        scanner.close();
    }
}