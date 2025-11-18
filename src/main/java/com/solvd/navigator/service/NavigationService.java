package com.solvd.navigator.service;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.util.FloydWarshall;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.RouteLocation;

import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationService {

    private final List<Location> locations;
    private final List<Edge> edges;
    // Maps ID → index
    private final Map<Long, Integer> locationToIndexMap;
    private final Map<Integer, Location> indexToLocationMap;
    private FloydWarshall.Result floydWarshallResult;
    private boolean isComputed;

    private static final Logger LOGGER = LogManager.getLogger(NavigationService.class);

    public NavigationService(List<Location> locations, List<Edge> edges) {
        this.locations = locations != null ? new ArrayList<>(locations) : new ArrayList<>();
        this.edges = edges != null ? new ArrayList<>(edges) : new ArrayList<>();
        this.locationToIndexMap = new HashMap<>();
        this.indexToLocationMap = new HashMap<>();
        this.isComputed = false;
        buildLocationMaps();
    }

    private void buildLocationMaps() {
        for (int index = 0; index < locations.size(); index++) {
            Location location = locations.get(index);

            locationToIndexMap.put(location.getId(), index);
            indexToLocationMap.put(index, location);
        }
    }

    public void computeShortestPaths() {
        if (locations.isEmpty()) {
            throw new IllegalStateException("There are no locations available for route calculation.");
        }

        floydWarshallResult = FloydWarshall.compute(
                locations.size(),
                edges,
                locationToIndexMap
        );

        isComputed = true;
    }

    public PathResult findShortestPath(String sourceLocationName, String targetLocationName) {
        if (!isComputed) {
            computeShortestPaths();
        }

        LOGGER.info("Finding path from '{}' to '{}'", sourceLocationName, targetLocationName);

        Location sourceLocation = findLocationByName(sourceLocationName);
        Location targetLocation = findLocationByName(targetLocationName);

        if (sourceLocation == null || targetLocation == null) {
            LOGGER.warn("Locations not found: source='{}', target='{}'",
                    sourceLocationName, targetLocationName);
            return new PathResult(0.0, new ArrayList<>(), false);
        }

        return findShortestPath(sourceLocation, targetLocation);
    }

    public PathResult findShortestPath(Location sourceLocation, Location targetLocation) {
        if (!isComputed) {
            computeShortestPaths();
        }

        Integer sourceIndex = locationToIndexMap.get(sourceLocation.getId());
        Integer targetIndex = locationToIndexMap.get(targetLocation.getId());

        LOGGER.debug("Indexes resolved: {} -> {}", sourceIndex, targetIndex);

        if (sourceIndex == null || targetIndex == null) {
            return new PathResult(0.0, new ArrayList<>(), false);
        }

        double[][] distanceMatrix = floydWarshallResult.distanceMatrix();
        Integer[][] nextNodeMatrix = floydWarshallResult.nextNodeMatrix();

        double totalDistance = distanceMatrix[sourceIndex][targetIndex];

        if (Double.isInfinite(totalDistance)) {
            LOGGER.warn("No path exists between '{}' and '{}'",
                    sourceLocation.getName(), targetLocation.getName());
            return new PathResult(0.0, new ArrayList<>(), false);
        }

        LOGGER.debug("Shortest distance from '{}' to '{}' = {}",
                sourceLocation.getName(),
                targetLocation.getName(),
                totalDistance);

        List<Integer> pathIndices = FloydWarshall.reconstructPath(
                nextNodeMatrix,
                sourceIndex,
                targetIndex
        );

        List<Location> pathLocations = new ArrayList<>();
        for (Integer pathIndex : pathIndices) {
            Location location = indexToLocationMap.get(pathIndex);
            if (location != null) {
                pathLocations.add(location);
            }
        }

        return new PathResult(totalDistance, pathLocations, true);
    }

    private Location findLocationByName(String locationName) {
        if (locationName == null) {
            return null;
        }

        for (Location location : locations) {
            if (locationName.equalsIgnoreCase(location.getName())) {
                return location;
            }
        }

        return null;
    }

    public boolean isComputed() {
        return isComputed;
    }

    // Static helper
    public static NavigationService fromDaos(ILocationDao locationDao,
                                             IEdgeDao edgeDao,
                                             IRouteDao routeDao,
                                             IRouteLocationDao routeLocationDao) {
        // 1) load locations (DB -> model)
        List<Location> locations = locationDao.findAll();

        // 2) load edges stored in DB (explicit edges / shortcuts)
        List<Edge> dbEdges = edgeDao.findAll();

        // 3) load routes and build edges from them
        List<Route> routes = routeDao.findAll();
        List<Edge> edgesFromRoutes = new ArrayList<>();
        for (Route route : routes) {
            // enrich route with its routeLocations (Route is immutable — rebuild with locations)
            List<RouteLocation> rlocs = routeLocationDao.findByRoute(route);
            Route enriched = new Route.Builder()
                    .withId(route.getId())
                    .withName(route.getName())
                    .withDescription(route.getDescription())
                    .withLocations(rlocs)
                    .build();
            edgesFromRoutes.addAll(RouteService.convertRouteToEdges(enriched));
        }

        // 4) merge edges: edgesFromRoutes + dbEdges
        List<Edge> allEdges = new ArrayList<>(edgesFromRoutes);
        allEdges.addAll(dbEdges);

        // 5) Build and return the NavigationService with loaded data
        return new NavigationService(locations, allEdges);
    }
}

