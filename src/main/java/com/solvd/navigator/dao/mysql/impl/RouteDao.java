package com.solvd.navigator.dao.mysql.impl;

import com.solvd.navigator.model.Route;
import com.solvd.navigator.dao.mysql.BaseDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class RouteDao extends BaseDao implements IRouteDao {

    private static final String FIND_ALL =
            "SELECT * FROM route";

    private static final String FIND_BY_ID =
            "SELECT * FROM route WHERE id=?";

    private static final String FIND_BY_NAME =
            "SELECT * FROM route WHERE name=?";

    private static final String INSERT =
            "INSERT INTO route (name, description) VALUES (?, ?)";

    private static final String DELETE =
            "DELETE FROM route WHERE id=?";

    private Route map(ResultSet rs) throws SQLException {
        return new Route.Builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .withDescription(rs.getString("description"))
                .build();
    }

    @Override
    public List<Route> findAll() {
        List<Route> routes = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ALL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) routes.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return routes;
    }

    @Override
    public Optional<Route> findById(Long id) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

                ps.setLong(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Route> findByName(String name) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_NAME)) {
                ps.setString(1, name);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return Optional.empty();
    }

    @Override
    public Route save(Route route) {
        Connection conn = null;
        Route routeSaved = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, route.getName());
                ps.setString(2, route.getDescription());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long generatedId = rs.getLong(1);
                        routeSaved = new Route.Builder()
                                .withId(generatedId)
                                .withName(route.getName())
                                .withDescription(route.getDescription())
                                .withLocations(route.getLocations())
                                .build();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return routeSaved;
    }

    @Override
    public void delete(Long id) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
    }
}