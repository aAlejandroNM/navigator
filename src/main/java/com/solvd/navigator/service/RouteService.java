package com.solvd.navigator.service;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.RouteLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RouteService {

    public static double calculateEuclideanDistance(Location sourceLocation, Location targetLocation) {
        double deltaX = targetLocation.getX() - sourceLocation.getX();
        double deltaY = targetLocation.getY() - sourceLocation.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static List<Edge> convertRouteToEdges(Route route) {
        List<Edge> edges = new ArrayList<>();

        if (route == null || route.getLocations() == null || route.getLocations().isEmpty()) {
            return edges;
        }

        List<RouteLocation> sortedRouteLocations = new ArrayList<>(route.getLocations());
        sortedRouteLocations.sort(Comparator.comparing(RouteLocation::getPosition));

        // Create Edges between consecutive locations
        for (int currentIndex = 0; currentIndex < sortedRouteLocations.size() - 1; currentIndex++) {
            RouteLocation currentRouteLocation = sortedRouteLocations.get(currentIndex);
            RouteLocation nextRouteLocation = sortedRouteLocations.get(currentIndex + 1);

            Location sourceLocation = currentRouteLocation.getLocation();
            Location targetLocation = nextRouteLocation.getLocation();

            if (sourceLocation != null && targetLocation != null) {
                double distance = calculateEuclideanDistance(sourceLocation, targetLocation);

                Edge edge = new Edge.Builder()
                        .withFrom(sourceLocation)
                        .withTo(targetLocation)
                        .withWeight(distance)
                        .withDirected(false)//son bidireccionales por el momento
                        .withName(route.getName() != null ? route.getName() : "Ruta automática")
                        .withActive(true)
                        .build();

                edges.add(edge);
            }
        }

        return edges;
    }

    public static List<Edge> convertRoutesToEdges(List<Route> routes) {
        List<Edge> allEdges = new ArrayList<>();

        if (routes == null) {
            return allEdges;
        }

        for (Route route : routes) {
            List<Edge> routeEdges = convertRouteToEdges(route);
            allEdges.addAll(routeEdges);
        }

        return allEdges;
    }
}

