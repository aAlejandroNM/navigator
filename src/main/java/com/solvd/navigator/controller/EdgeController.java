package com.solvd.navigator.controller;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.service.EdgeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EdgeController {

    private static final Logger LOGGER = LogManager.getLogger(EdgeController.class);

    private final EdgeService edgeService;

    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    public List<Edge> getAllEdges() {
        return edgeService.listEdges();
    }

    public Edge createEdge(Long fromId,
                           Long toId,
                           Double weight,
                           Boolean directed,
                           String name,
                           Boolean active) {
        LOGGER.info("Creating edge: {} -> {} (w={}, dir={})", fromId, toId, weight, directed);
        return edgeService.createEdge(fromId, toId, weight, directed, name, active);
    }

    public boolean updateEdge(Long id,
                              Long newFromId,
                              Long newToId,
                              Double newWeight,
                              Boolean newDirected,
                              String newName,
                              Boolean newActive) {
        LOGGER.info("Updating edge id={}", id);
        return edgeService.updateEdge(id, newFromId, newToId, newWeight, newDirected, newName, newActive);
    }

    public boolean deleteEdge(Long id) {
        LOGGER.info("Deleting edge id={}", id);
        return edgeService.deleteEdge(id);
    }
}