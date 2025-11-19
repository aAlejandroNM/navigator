package com.solvd.navigator.service;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class EdgeService {

    private static final Logger LOGGER = LogManager.getLogger(EdgeService.class);

    private final IEdgeDao edgeDao;
    private final ILocationDao locationDao;

    public EdgeService(IEdgeDao edgeDao, ILocationDao locationDao) {
        this.edgeDao = edgeDao;
        this.locationDao = locationDao;
    }

    public List<Edge> listEdges() {
        return edgeDao.findAll();
    }

    public Optional<Edge> findById(Long id) {
        return edgeDao.findById(id);
    }

    public Edge createEdge(Long fromId,
                           Long toId,
                           Double weight,
                           Boolean directed,
                           String name,
                           Boolean active) {

        Location from = locationDao.findById(fromId).orElse(null);
        Location to = locationDao.findById(toId).orElse(null);

        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' locations must exist.");
        }

        Edge edge = new Edge.Builder()
                .withFrom(from)
                .withTo(to)
                .withWeight(weight)
                .withDirected(directed)
                .withName(name)
                .withActive(active)
                .build();

        return edgeDao.save(edge);
    }

    public boolean updateEdge(Long id,
                              Long newFromId,
                              Long newToId,
                              Double newWeight,
                              Boolean newDirected,
                              String newName,
                              Boolean newActive) {

        Optional<Edge> opt = edgeDao.findById(id);
        if (opt.isEmpty()) {
            return false;
        }

        Edge old = opt.get();

        Location from = newFromId != null ? locationDao.findById(newFromId).orElse(null) : old.getFrom();
        Location to = newToId != null ? locationDao.findById(newToId).orElse(null) : old.getTo();

        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' locations must exist.");
        }

        Edge updated = new Edge.Builder()
                .withId(old.getId())
                .withFrom(from)
                .withTo(to)
                .withWeight(newWeight != null ? newWeight : old.getWeight())
                .withDirected(newDirected != null ? newDirected : old.isDirected())
                .withName(newName != null ? newName : old.getName())
                .withActive(newActive != null ? newActive : old.isActive())
                .build();

        edgeDao.update(updated);
        return true;
    }

    public boolean deleteEdge(Long id) {
        Optional<Edge> opt = edgeDao.findById(id);
        if (opt.isEmpty()) return false;
        edgeDao.delete(id);
        return true;
    }
}