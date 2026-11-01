package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;

/** Shared behaviour for every DAO (inheritance, Week 8). */
public abstract class BaseDAO {
    protected Connection connect() throws DatabaseException {
        return DatabaseConnection.getInstance().getConnection();
    }

    protected DatabaseException wrap(SQLException e) {
        return new DatabaseException("Database error: " + e.getMessage(), e);
    }
}
