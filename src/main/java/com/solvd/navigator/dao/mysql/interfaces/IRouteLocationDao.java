package com.solvd.navigator.dao.mysql.interfaces;

import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.RouteLocation;

import java.util.List;

public interface IRouteLocationDao {
    List<RouteLocation> findByLocation(Location location);
    List<RouteLocation> findByRoute(Route route);
    RouteLocation save(RouteLocation routeLocation);
    void deleteByRoute(Route route);
    void deleteByLocation(Location location);
}
