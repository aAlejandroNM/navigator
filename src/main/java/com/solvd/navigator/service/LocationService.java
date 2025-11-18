package com.solvd.navigator.service;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.RouteLocation;

import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import java.util.List;
import java.util.Optional;

public class LocationService {

    private final ILocationDao locationDao;
    private final IEdgeDao edgeDao;
    private final IRouteLocationDao routeLocationDao;

    public LocationService(ILocationDao locationDao,
                           IEdgeDao edgeDao,
                           IRouteLocationDao routeLocationDao) {
        this.locationDao = locationDao;
        this.edgeDao = edgeDao;
        this.routeLocationDao = routeLocationDao;
    }

    public Location addLocation(String name,
                                String description,
                                Double x,
                                Double y,
                                String type) {

        Location newLoc = new Location.Builder()
                .withName(name)
                .withDescription(description)
                .withX(x)
                .withY(y)
                .withType(type)
                .build();

        return locationDao.save(newLoc);
    }

    public boolean updateLocation(Long id,
                                  String newName,
                                  String newDescription,
                                  Double newX,
                                  Double newY,
                                  String newType) {

        Optional<Location> opt = locationDao.findById(id);
        if (opt.isEmpty()) return false;

        Location old = opt.get();

        Location updated = new Location.Builder()
                .withId(old.getId())
                .withName(newName != null ? newName : old.getName())
                .withDescription(newDescription != null ? newDescription : old.getDescription())
                .withX(newX != null ? newX : old.getX())
                .withY(newY != null ? newY : old.getY())
                .withType(newType != null ? newType : old.getType())
                .build();

        locationDao.update(updated);
        return true;
    }

    public boolean deleteLocation(Long id) {
        Optional<Location> opt = locationDao.findById(id);
        if (opt.isEmpty()) return false;

        Location loc = opt.get();

        // 1. Edge dependency check
        List<Edge> edges = edgeDao.findByLocation(loc);
        if (!edges.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete location: it is used by edges."
            );
        }

        // 2. Route dependency check
        List<RouteLocation> routeLocations = routeLocationDao.findByLocation(loc);
        if (!routeLocations.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete location: it belongs to one or more routes."
            );
        }

        locationDao.delete(id);
        return true;
    }

    public List<Location> listLocations() {
        return locationDao.findAll();
    }
}
