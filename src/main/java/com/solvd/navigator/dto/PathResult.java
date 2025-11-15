package com.solvd.navigator.dto;

import com.solvd.navigator.model.Location;

import java.util.List;

public class PathResult {
    private final double totalDistance;
    private final List<Location> pathLocations;
    private final boolean pathExists;

    public PathResult(double totalDistance, List<Location> pathLocations, boolean pathExists) {
        this.totalDistance = totalDistance;
        this.pathLocations = pathLocations;
        this.pathExists = pathExists;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public List<Location> getPathLocations() {
        return pathLocations;
    }

    public boolean isPathExists() {
        return pathExists;
    }

    @Override
    public String toString() {
        if (!pathExists) {
            return "No existe ruta entre las ubicaciones especificadas.";
        }
        
        StringBuilder pathString = new StringBuilder();
        pathString.append("Distancia total: ").append(totalDistance).append("\n");
        pathString.append("Ruta: ");
        
        for (int index = 0; index < pathLocations.size(); index++) {
            pathString.append(pathLocations.get(index).getName());
            if (index < pathLocations.size() - 1) {
                pathString.append(" -> ");
            }
        }
        
        return pathString.toString();
    }
}

