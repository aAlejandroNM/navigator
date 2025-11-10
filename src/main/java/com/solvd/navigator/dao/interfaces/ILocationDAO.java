package com.solvd.navigator.dao.interfaces;

import com.solvd.navigator.model.Location;

import java.util.List;
import java.util.Optional;

public interface ILocationDAO {
    List<Location> findAll();
    Optional<Location> findById(Long id);
    Optional<Location> findByName(String name);
    List<Location> findByType(String type);
    boolean existsByName(String name);
    Location save(Location location);
    void update(Location location);
    void delete(Long id);
}
