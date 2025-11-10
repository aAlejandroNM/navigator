package com.solvd.navigator.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionPool {

    private static ConnectionPool instance;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 5;
    private final String url;
    private final String user;
    private final String password;


    private ConnectionPool() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading application.properties", e);
        }

        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
        int poolSize = Integer.parseInt(props.getProperty("db.pool.size", String.valueOf(INITIAL_POOL_SIZE)));


        connectionPool = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(createConnection());
        }
    }

    public static synchronized ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        if (connectionPool.isEmpty()) {
            throw new RuntimeException("Maximum pool size reached, no available connections.");
        }
        Connection connection = connectionPool.removeLast();
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        connectionPool.add(connection);
        usedConnections.remove(connection);
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
