package com.solvd.navigator.util;

import com.solvd.navigator.model.Edge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    public static final double INFINITY = Double.POSITIVE_INFINITY;
    private static final Logger LOGGER = LogManager.getLogger(FloydWarshall.class);

    public record Result(double[][] distanceMatrix, Integer[][] nextNodeMatrix) {}

    public static Result compute(int numberOfNodes, List<Edge> edges, Map<Long, Integer> idToIndex) {

        LOGGER.debug("Running Floyd–Warshall with {} nodes and {} edges",
                numberOfNodes, edges.size());

        double[][] distanceMatrix = new double[numberOfNodes][numberOfNodes];
        Integer[][] nextNodeMatrix = new Integer[numberOfNodes][numberOfNodes];

        // Initialize distance matrix
        for (int sourceIndex = 0; sourceIndex < numberOfNodes; sourceIndex++) {
            for (int targetIndex = 0; targetIndex < numberOfNodes; targetIndex++) {
                if (sourceIndex == targetIndex) {
                    distanceMatrix[sourceIndex][targetIndex] = 0.0;
                } else {
                    distanceMatrix[sourceIndex][targetIndex] = INFINITY;
                }
            }
        }

        // Establish initial distances from the edges
        for (Edge edge : edges) {
            Long fromId = edge.getFrom() != null ? edge.getFrom().getId() : null;
            Long toId   = edge.getTo()   != null ? edge.getTo().getId()   : null;

            if (fromId == null || toId == null) {
                continue;
            }

            Integer fromIndex = idToIndex.get(fromId);
            Integer toIndex = idToIndex.get(toId);

            if (fromIndex != null && toIndex != null) {
                double currentWeight = distanceMatrix[fromIndex][toIndex];
                double edgeWeight = edge.getWeight();
                
                if (edgeWeight < currentWeight) {
                    distanceMatrix[fromIndex][toIndex] = edgeWeight;
                    nextNodeMatrix[fromIndex][toIndex] = toIndex;
                }

                if (edge.isDirected() == null || !edge.isDirected()) {
                    double reverseWeight = distanceMatrix[toIndex][fromIndex];
                    if (edgeWeight < reverseWeight) {
                        distanceMatrix[toIndex][fromIndex] = edgeWeight;
                        nextNodeMatrix[toIndex][fromIndex] = fromIndex;
                    }
                }
            }
        }

        //  find shorter routes
        for (int intermediateIndex = 0; intermediateIndex < numberOfNodes; intermediateIndex++) {
            for (int sourceIndex = 0; sourceIndex < numberOfNodes; sourceIndex++) {
                // If there is no path from sourceIndex to intermediateIndex, continue
                if (distanceMatrix[sourceIndex][intermediateIndex] == INFINITY) {
                    continue;
                }

                for (int targetIndex = 0; targetIndex < numberOfNodes; targetIndex++) {
                    // If there is no path from intermediateIndex to targetIndex, continue
                    if (distanceMatrix[intermediateIndex][targetIndex] == INFINITY) {
                        continue;
                    }

                    double distanceThroughIntermediate = 
                        distanceMatrix[sourceIndex][intermediateIndex] + 
                        distanceMatrix[intermediateIndex][targetIndex];

                    if (distanceThroughIntermediate < distanceMatrix[sourceIndex][targetIndex]) {
                        LOGGER.trace("Relaxing: {} -> {} via {} | new distance = {}",
                                sourceIndex, targetIndex, intermediateIndex, distanceThroughIntermediate);
                        distanceMatrix[sourceIndex][targetIndex] = distanceThroughIntermediate;
                        nextNodeMatrix[sourceIndex][targetIndex] = nextNodeMatrix[sourceIndex][intermediateIndex];
                    }
                }
            }
        }

        return new Result(distanceMatrix, nextNodeMatrix);
    }

    public static List<Integer> reconstructPath(Integer[][] nextNodeMatrix, int sourceIndex, int targetIndex) {
        if (nextNodeMatrix[sourceIndex][targetIndex] == null) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        int current = sourceIndex;
        path.add(current);

        while (current != targetIndex) {
            Integer next = nextNodeMatrix[current][targetIndex];
            if (next == null) {
                return Collections.emptyList(); // no valid path
            }
            current = next;
            path.add(current);
        }

        return path;
    }
}