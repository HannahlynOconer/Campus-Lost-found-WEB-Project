package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminDAO extends BaseDAO implements Repository<Admin, Integer> {

    @Override
    public Admin save(Admin a) throws DatabaseException {
        String sql = "INSERT INTO admins (username, full_name, password_hash, salt) VALUES (?, ?, ?, ?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getUsername());
            ps.setString(2, a.getFullName());
            ps.setString(3, a.getPasswordHash());
            ps.setString(4, a.getSalt());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) a.setId(keys.getInt(1));
            }
            return a;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public Optional<Admin> findById(Integer id) throws DatabaseException {
        return queryOne("SELECT * FROM admins WHERE id = ?", String.valueOf(id));
    }

    public Optional<Admin> findByUsername(String username) throws DatabaseException {
        return queryOne("SELECT * FROM admins WHERE username = ?", username);
    }

    @Override
    public List<Admin> findAll() throws DatabaseException {
        List<Admin> list = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT * FROM admins");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    public int count() throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM admins");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Optional<Admin> queryOne(String sql, String param) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Admin map(ResultSet rs) throws SQLException {
        return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("full_name"),
                rs.getString("password_hash"), rs.getString("salt"));
    }
}
