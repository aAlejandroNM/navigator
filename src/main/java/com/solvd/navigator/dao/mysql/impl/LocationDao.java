package com.solvd.navigator.dao.mysql.impl;
import com.solvd.navigator.dao.mysql.BaseDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.model.Location;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao extends BaseDao implements ILocationDao {

    private static final String INSERT =
            "INSERT INTO location (name, x, y, type, description) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE location SET name=?, x=?, y=?, type=?, description=? WHERE id=?";
    private static final String DELETE =
            "DELETE FROM location WHERE id=?";
    private static final String FIND_BY_ID =
            "SELECT * FROM location WHERE id=?";
    private static final String FIND_ALL =
            "SELECT * FROM location";
    private static final String FIND_BY_NAME =
            "SELECT * FROM location WHERE name=?";
    private static final String FIND_BY_TYPE =
            "SELECT * FROM location WHERE type=?";
    private static final String EXISTS_BY_NAME =
            "SELECT COUNT(*) FROM location WHERE name=?";

    private Location map(ResultSet rs) throws SQLException {
        return new Location.Builder()
                .withId(rs.getLong("id"))
                .withName(rs.getString("name"))
                .withX(rs.getDouble("x"))
                .withY(rs.getDouble("y"))
                .withType(rs.getString("type"))
                .withDescription(rs.getString("description"))
                .build();
    }

    @Override
    public List<Location> findAll() {
        List<Location> locations = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ALL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    locations.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return locations;
    }

    @Override
    public Optional<Location> findById(Long id) {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(map(rs));
                    }
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
    public Optional<Location> findByName(String name) {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_NAME)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(map(rs));
                    }
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
    public List<Location> findByType(String type) {
        List<Location> locations = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_TYPE)) {
                ps.setString(1, type);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        locations.add(map(rs));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return locations;
    }

    @Override
    public boolean existsByName(String name) {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(EXISTS_BY_NAME)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return false;
    }

    @Override
    public Location save(Location location) {
        Connection conn = null;
        Location savedLocation = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, location.getName());
                ps.setDouble(2, location.getX());
                ps.setDouble(3, location.getY());
                ps.setString(4, location.getType());
                ps.setString(5, location.getDescription());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        Long generatedId = rs.getLong(1);
                        savedLocation = new Location.Builder()
                                .withId(generatedId)
                                .withName(location.getName())
                                .withX(location.getX())
                                .withY(location.getY())
                                .withType(location.getType())
                                .withDescription(location.getDescription())
                                .build();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return savedLocation;
    }
    
    @Override
    public void update(Location location) {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
                ps.setString(1, location.getName());
                ps.setDouble(2, location.getX());
                ps.setDouble(3, location.getY());
                ps.setString(4, location.getType());
                ps.setString(5, location.getDescription());
                ps.setLong(6, location.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
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