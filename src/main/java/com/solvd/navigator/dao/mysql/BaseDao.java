package com.solvd.navigator.dao.mysql;

import com.solvd.navigator.util.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDao {

    protected final ConnectionPool cp;

    protected BaseDao() {
        try {
            this.cp = ConnectionPool.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing connection pool", e);
        }
    }

    protected Connection getConnection() {
        return cp.getConnection();
    }

    protected void releaseConnection(Connection connection) {
        if (connection != null) {
            cp.releaseConnection(connection);
        }
    }
}
