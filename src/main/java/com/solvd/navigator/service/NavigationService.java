package com.solvd.navigator.service;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.util.FloydWarshall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationService {

    private final List<Location> locations;
    private final List<Edge> edges;
    private final Map<Location, Integer> locationToIndexMap;
    private final Map<Integer, Location> indexToLocationMap;
    private FloydWarshall.Result floydWarshallResult;
    private boolean isComputed;

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
            locationToIndexMap.put(location, index);
            indexToLocationMap.put(index, location);
        }
    }

    public void computeShortestPaths() {
        if (locations.isEmpty()) {
            throw new IllegalStateException("No hay ubicaciones disponibles para calcular rutas.");
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

        Location sourceLocation = findLocationByName(sourceLocationName);
        Location targetLocation = findLocationByName(targetLocationName);

        if (sourceLocation == null || targetLocation == null) {
            return new PathResult(0.0, new ArrayList<>(), false);
        }

        return findShortestPath(sourceLocation, targetLocation);
    }

    public PathResult findShortestPath(Location sourceLocation, Location targetLocation) {
        if (!isComputed) {
            computeShortestPaths();
        }

        Integer sourceIndex = locationToIndexMap.get(sourceLocation);
        Integer targetIndex = locationToIndexMap.get(targetLocation);

        if (sourceIndex == null || targetIndex == null) {
            return new PathResult(0.0, new ArrayList<>(), false);
        }

        double[][] distanceMatrix = floydWarshallResult.getDistanceMatrix();
        Integer[][] nextNodeMatrix = floydWarshallResult.getNextNodeMatrix();

        double totalDistance = distanceMatrix[sourceIndex][targetIndex];

        if (Double.isInfinite(totalDistance)) {
            return new PathResult(0.0, new ArrayList<>(), false);
        }

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

    public List<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }

    public boolean isComputed() {
        return isComputed;
    }
}

