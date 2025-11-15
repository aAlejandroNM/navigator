package com.solvd.navigator.dao.mysql.interfaces;

import com.solvd.navigator.model.Route;

import java.util.List;
import java.util.Optional;

public interface IRouteDao {
    List<Route> findAll();
    Optional<Route> findById(Long id);
    Optional<Route> findByName(String name);
    Route save(Route route);
    void delete(Long id);
}