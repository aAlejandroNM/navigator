package com.solvd.navigator.util;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
public class FloydWarshall {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    public static class Result {
        private final double[][] distanceMatrix;
        private final Integer[][] nextNodeMatrix;

        public Result(double[][] distanceMatrix, Integer[][] nextNodeMatrix) {
            this.distanceMatrix = distanceMatrix;
            this.nextNodeMatrix = nextNodeMatrix;
        }

        public double[][] getDistanceMatrix() {
            return distanceMatrix;
        }

        public Integer[][] getNextNodeMatrix() {
            return nextNodeMatrix;
        }
    }

    public static Result compute(int numberOfNodes, List<Edge> edges, Map<Location, Integer> locationToIndex) {
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
            Integer fromIndex = locationToIndex.get(edge.getFrom());
            Integer toIndex = locationToIndex.get(edge.getTo());

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
        Integer currentNodeIndex = sourceIndex;
        path.add(currentNodeIndex);

        while (currentNodeIndex != null && currentNodeIndex != targetIndex) {
            currentNodeIndex = nextNodeMatrix[currentNodeIndex][targetIndex];
            if (currentNodeIndex == null) {
                return Collections.emptyList();
            }
            path.add(currentNodeIndex);
        }

        return path;
    }
}
