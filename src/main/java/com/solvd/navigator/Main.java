package com.solvd.navigator;

import com.solvd.navigator.dao.mysql.impl.*;
import com.solvd.navigator.model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // --- provisional tests for DAOs ---
        LOGGER.info("=== Navigator DAO Test Started ===");

        // --- DAOs ---
        LocationDao locationDAO = new LocationDao();
        EdgeDao edgeDAO = new EdgeDao(locationDAO);
        RouteDao routeDAO = new RouteDao();
        RouteLocationDao routeLocationDAO = new RouteLocationDao(locationDAO);

        // --- LOCATION TEST ---
        Location location = new Location.Builder()
                .withName("East Park")
                .withX(60.785091)
                .withY(-103.968285)
                .withType("Park")
                .withDescription("A large public park in the east area")
                .build();

        LOGGER.info("--- Saving new Location ---");
        Location savedLocation = locationDAO.save(location);
        LOGGER.info(savedLocation != null ?
                "Location saved with ID: " + savedLocation.getId() :
                "Failed to save location");

        // --- EDGE TEST ---
        Location from = locationDAO.findByName("Central Park").orElse(null);
        Location to = locationDAO.findByName("Times Square").orElse(null);

        if (from != null && to != null) {
            Edge edge = new Edge.Builder()
                    .withFrom(from)
                    .withTo(to)
                    .withWeight(1.5)
                    .withDirected(false)
                    .withName("Central-Times")
                    .withActive(true)
                    .build();

            LOGGER.info("--- Saving new Edge ---");
            Edge savedEdge = edgeDAO.save(edge);
            LOGGER.info(savedEdge != null ?
                    "Edge saved with ID: " + savedEdge.getId() :
                    "Failed to save edge");

            savedEdge = edgeDAO.findById(savedEdge.getId()).orElse(null);
            LOGGER.info(savedEdge != null ?
                    "Edge found: " + savedEdge.getName() :
                    "Edge not found");

            if (savedEdge != null) {
                Edge updatedEdge = new Edge.Builder()
                        .withId(savedEdge.getId())
                        .withFrom(savedEdge.getFrom())
                        .withTo(savedEdge.getTo())
                        .withWeight(2.0)
                        .withDirected(savedEdge.isDirected())
                        .withName("Updated Edge")
                        .withActive(false)
                        .build();
                edgeDAO.update(updatedEdge);
                LOGGER.info("Edge updated: " + updatedEdge.getName());
            }

            if (savedEdge != null) {
                edgeDAO.delete(savedEdge.getId());
                LOGGER.info("Edge deleted: " + savedEdge.getId());
            }
        }

        // --- ROUTE TEST ---
        Route route = new Route.Builder()
                .withName("Test Route")
                .withDescription("A test route for DAO")
                .build();

        LOGGER.info("--- Saving new Route ---");
        routeDAO.save(route);
        LOGGER.info("Route saved: " + route.getName());

        Optional<Route> fetchedRoute = routeDAO.findByName("Test Route");
        fetchedRoute.ifPresent(r -> LOGGER.info("Route found: " + r.getName()));

        fetchedRoute.ifPresent(r -> {
            routeDAO.delete(r.getId());
            LOGGER.info("Route deleted: " + r.getId());
        });

        // --- ROUTE LOCATION TEST ---
        Route testRoute = new Route.Builder()
                .withName("RL Test Route")
                .withDescription("Route for RouteLocation DAO test")
                .build();

        // capture returned Route with ID
        Route savedTestRoute = routeDAO.save(testRoute);

        if (from != null && testRoute != null) {
            RouteLocation rl = new RouteLocation.Builder()
                    .withRoute(savedTestRoute)
                    .withLocation(from)
                    .withPosition(1L)
                    .build();

            LOGGER.info("--- Saving RouteLocation ---");
            routeLocationDAO.save(rl);
            LOGGER.info("RouteLocation saved for route: " + testRoute.getName());

            List<RouteLocation> rlList = routeLocationDAO.findByRoute(savedTestRoute);
            rlList.forEach(rlo -> LOGGER.info("RouteLocation: " + rlo.getLocation().getName() + " at position " + rlo.getPosition()));

            routeLocationDAO.deleteByRoute(savedTestRoute);
            LOGGER.info("Deleted all RouteLocations for route: " + testRoute.getName());
        }

        LOGGER.info("=== Navigator DAO Test Finished ===");
    }
}