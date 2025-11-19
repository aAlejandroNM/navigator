package com.solvd.navigator.controller;

import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.RouteLocation;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteController {

    private final IRouteDao routeDao;
    private final ILocationDao locationDao;
    private final IRouteLocationDao routeLocationDao;

    public RouteController(IRouteDao routeDao, ILocationDao locationDao, IRouteLocationDao routeLocationDao) {
        this.routeDao = routeDao;
        this.locationDao = locationDao;
        this.routeLocationDao = routeLocationDao;
    }

    public List<Route> getAllRoutes() {
        return routeDao.findAll();
    }

    public Route addRoute(String name, String description, List<Location> locations) {

        if (locations == null || locations.isEmpty()) {
            throw new IllegalArgumentException("A route must contain at least 1 location");
        }
        
        List<RouteLocation> routeLocations = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            routeLocations.add(new RouteLocation.Builder()
                    .withLocation(locations.get(i))
                    .withPosition((long) i)
                    .build());
        }

        Route route = new Route.Builder()
                .withName(name)
                .withDescription(description)
                .withLocations(new ArrayList<>())
                .build();

        Route savedRoute = routeDao.save(route);

        List<RouteLocation> savedRouteLocations = new ArrayList<>();
        for (RouteLocation rl : routeLocations) {
            RouteLocation rlWithRoute = new RouteLocation.Builder()
                    .withRoute(savedRoute)
                    .withLocation(rl.getLocation())
                    .withPosition(rl.getPosition())
                    .build();

            routeLocationDao.save(rlWithRoute);
            savedRouteLocations.add(rlWithRoute);
        }

        return new Route.Builder()
                .withId(savedRoute.getId())
                .withName(savedRoute.getName())
                .withDescription(savedRoute.getDescription())
                .withLocations(savedRouteLocations)
                .build();
    }

    public boolean deleteRoute(Long id) {
        Optional<Route> routeOpt = routeDao.findById(id);
        if (routeOpt.isEmpty()) return false;

        routeLocationDao.deleteByRoute(routeOpt.get());

        routeDao.delete(id);
        return true;
    }

    public Optional<Location> findLocationById(Long id) {
        return locationDao.findById(id);
    }
}