package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** SINGLETON: one object that knows how to open JDBC connections. Settings come from db.properties (file handling). */
public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() throws DatabaseException {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) throw new DatabaseException("db.properties was not found in src/main/resources.");
            props.load(in);
        } catch (IOException e) {
            throw new DatabaseException("Cannot read db.properties.", e);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("MySQL driver not found in WEB-INF/lib.", e);
        }
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password", "");
    }

    public static synchronized DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot connect to MySQL. Is it running and is schema.sql imported? (" + e.getMessage() + ")", e);
        }
    }
}
