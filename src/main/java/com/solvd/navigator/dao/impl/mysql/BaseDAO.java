package com.solvd.navigator.dao.impl.mysql;

import com.solvd.navigator.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {

    private final ConnectionPool connectionPool;

    protected BaseDAO() {
        try {
            this.connectionPool = ConnectionPool.getConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize ConnectionPool", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    protected void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.releaseConnection(conn);
        }
    }
}