package com.solvd.navigator.dao.interfaces;

import com.solvd.navigator.model.Route;

import java.util.List;
import java.util.Optional;

public interface IRouteDAO {
    List<Route> findAll();
    Optional<Route> findById(Long id);
    Optional<Route> findByName(String name);
    void save(Route route);
    void delete(Long id);
}