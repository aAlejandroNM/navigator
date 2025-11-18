package com.solvd.navigator.dto;

import com.solvd.navigator.model.Location;

import java.util.List;

public record PathResult(
        double totalDistance,
        List<Location> pathLocations,
        boolean pathExists
) {
    @Override
    public String toString() {
        if (!pathExists) {
            return "There is no available route between the specified locations.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total distance: ").append(totalDistance).append("\n");
        sb.append("Route: ");

        for (int i = 0; i < pathLocations.size(); i++) {
            sb.append(pathLocations.get(i).getName());
            if (i < pathLocations.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }
}

