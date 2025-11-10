package com.solvd.navigator.util;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPool {
    private static ConnectionPool cp;
    private final ArrayBlockingQueue<Connection> pool;

    private static final int POOL_SIZE = 10;
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    private ConnectionPool() throws SQLException {
        loadProperties();

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }

        pool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.add(DriverManager.getConnection(URL, USER, PASSWORD));
        }
    }

    private void loadProperties() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new SQLException("Unable to find application.properties");
            }
            props.load(input);
            DRIVER = props.getProperty("driver");
            URL = props.getProperty("url");
            USER = props.getProperty("username");
            PASSWORD = props.getProperty("password");

        } catch (IOException e) {
            throw new SQLException("Failed to load DB properties", e);
        }
    }

    public static synchronized ConnectionPool getConnectionPool() throws SQLException {
        if (cp == null) {
            cp = new ConnectionPool();
        }
        return cp;
    }

    public Connection getConnection() throws SQLException {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new SQLException("Interrupted while waiting for a DB connection", e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }
}