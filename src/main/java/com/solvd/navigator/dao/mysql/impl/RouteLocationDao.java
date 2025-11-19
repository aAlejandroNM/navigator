package com.solvd.navigator.dao.mysql.impl;

import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.dao.mysql.BaseDao;
import com.solvd.navigator.model.RouteLocation;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;

public class RouteLocationDao extends BaseDao implements IRouteLocationDao {

    private final ILocationDao locationDao;

    public RouteLocationDao(ILocationDao locationDao) {
        this.locationDao = locationDao;
    }

    private static final String FIND_BY_LOCATION =
            "SELECT * FROM route_location WHERE location_id=? ORDER BY position ASC";

    private static final String FIND_BY_ROUTE =
            "SELECT * FROM route_location WHERE route_id=? ORDER BY position ASC";

    private static final String INSERT =
            "INSERT INTO route_location (route_id, location_id, position) VALUES (?, ?, ?)";

    private static final String DELETE_BY_ROUTE =
            "DELETE FROM route_location WHERE route_id=?";

    private static final String DELETE_BY_LOCATION =
            "DELETE FROM route_location WHERE location_id=?";

    private RouteLocation map(ResultSet rs, Route route) throws SQLException {

        Location location = locationDao.findById(rs.getLong("location_id")).orElse(null);

        return new RouteLocation.Builder()
                .withRoute(route)
                .withLocation(location)
                .withPosition(rs.getLong("position"))
                .build();
    }

    @Override
    public List<RouteLocation> findByLocation(Location location) {
        List<RouteLocation> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();

            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_LOCATION)) {
                ps.setLong(1, location.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Route route = new Route.Builder()
                                .withId(rs.getLong("route_id"))
                                .build();

                        list.add(map(rs, route));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return list;
    }

    @Override
    public List<RouteLocation> findByRoute(Route route) {
        List<RouteLocation> list = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_ROUTE)) {

                ps.setLong(1, route.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(map(rs, route));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return list;
    }

    @Override
    public RouteLocation save(RouteLocation routeLocation) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
                ps.setLong(1, routeLocation.getRoute().getId());
                ps.setLong(2, routeLocation.getLocation().getId());
                ps.setLong(3, routeLocation.getPosition());

                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return routeLocation;
    }

    @Override
    public void deleteByRoute(Route route) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(DELETE_BY_ROUTE)) {
                ps.setLong(1, route.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
    }

    @Override
    public void deleteByLocation(Location location) {
        Connection conn = null;

        try {
            conn = getConnection();

            try (PreparedStatement ps = conn.prepareStatement(DELETE_BY_LOCATION)) {
                ps.setLong(1, location.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
    }
}
