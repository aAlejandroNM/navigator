package com.solvd.navigator.dao.interfaces;

import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.RouteLocation;

import java.util.List;

public interface IRouteLocationDAO {
    List<RouteLocation> findByRoute(Route route);
    void save(RouteLocation routeLocation);
    void deleteByRoute(Route route);
}
