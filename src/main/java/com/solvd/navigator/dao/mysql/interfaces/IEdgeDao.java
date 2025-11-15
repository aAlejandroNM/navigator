package com.solvd.navigator.dao.mysql.interfaces;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;

import java.util.List;
import java.util.Optional;

public interface IEdgeDao {
    List<Edge> findAll();
    List<Edge> findByLocation(Location location);
    List<Edge> findBetween(Location from, Location to);
    List<Edge> findActive();
    List<Edge> findDirected(boolean directed);
    Optional<Edge> findById(Long id);
    Edge save(Edge edge);
    void update(Edge edge);
    void delete(Long id);
}
