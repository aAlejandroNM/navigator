package com.solvd.navigator.dao.mysql.impl;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.dao.mysql.BaseDao;
import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class EdgeDao extends BaseDao implements IEdgeDao {

    private final ILocationDao locationDao;

    public EdgeDao(ILocationDao locationDao) {
        this.locationDao = locationDao;
    }

    private static final String FIND_ALL =
            "SELECT * FROM edge";

    private static final String FIND_BY_ID =
            "SELECT * FROM edge WHERE id=?";

    private static final String FIND_BY_LOCATION =
            "SELECT * FROM edge WHERE from_location=? OR to_location=?";

    private static final String FIND_BETWEEN =
            "SELECT * FROM edge WHERE from_location=? AND to_location=?";

    private static final String FIND_ACTIVE =
            "SELECT * FROM edge WHERE active=TRUE";

    private static final String FIND_DIRECTED =
            "SELECT * FROM edge WHERE directed=?";

    private static final String INSERT =
            "INSERT INTO edge (from_location, to_location, weight, directed, name, active) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE edge SET from_location=?, to_location=?, weight=?, directed=?, name=?, active=? WHERE id=?";

    private static final String DELETE =
            "DELETE FROM edge WHERE id=?";

    private Edge map(ResultSet rs) throws SQLException {
        Location from = locationDao.findById(rs.getLong("from_location")).orElse(null);
        Location to = locationDao.findById(rs.getLong("to_location")).orElse(null);

        return new Edge.Builder()
                .withId(rs.getLong("id"))
                .withFrom(from)
                .withTo(to)
                .withWeight(rs.getDouble("weight"))
                .withDirected(rs.getBoolean("directed"))
                .withName(rs.getString("name"))
                .withActive(rs.getBoolean("active"))
                .build();
    }

    @Override
    public List<Edge> findAll() {
        List<Edge> edges = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ALL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) edges.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return edges;
    }

    @Override
    public Optional<Edge> findById(Long id) {
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
    public List<Edge> findByLocation(Location location) {
        List<Edge> edges = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BY_LOCATION)) {
                ps.setLong(1, location.getId());
                ps.setLong(2, location.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) edges.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return edges;
    }

    @Override
    public List<Edge> findBetween(Location from, Location to) {
        List<Edge> edges = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_BETWEEN)) {
                ps.setLong(1, from.getId());
                ps.setLong(2, to.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) edges.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return edges;
    }

    @Override
    public List<Edge> findActive() {
        List<Edge> edges = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_ACTIVE);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) edges.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return edges;
    }

    @Override
    public List<Edge> findDirected(boolean directed) {
        List<Edge> edges = new ArrayList<>();
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_DIRECTED)) {
                ps.setBoolean(1, directed);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) edges.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return edges;
    }

    @Override
    public Edge save(Edge edge) {
        Connection conn = null;
        Edge edgeSaved = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps =
                         conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

                ps.setLong(1, edge.getFrom().getId());
                ps.setLong(2, edge.getTo().getId());
                ps.setDouble(3, edge.getWeight());
                ps.setBoolean(4, edge.isDirected());
                ps.setString(5, edge.getName());
                ps.setBoolean(6, edge.isActive());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1);

                        edgeSaved = new Edge.Builder()
                                .withId(id)
                                .withFrom(edge.getFrom())
                                .withTo(edge.getTo())
                                .withWeight(edge.getWeight())
                                .withDirected(edge.isDirected())
                                .withName(edge.getName())
                                .withActive(edge.isActive())
                                .build();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }

        return edgeSaved;
    }


    @Override
    public void update(Edge edge) {
        Connection conn = null;

        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
                ps.setLong(1, edge.getFrom().getId());
                ps.setLong(2, edge.getTo().getId());
                ps.setDouble(3, edge.getWeight());
                ps.setBoolean(4, edge.isDirected());
                ps.setString(5, edge.getName());
                ps.setBoolean(6, edge.isActive());
                ps.setLong(7, edge.getId());

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
