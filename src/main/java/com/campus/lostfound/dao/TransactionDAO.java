package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.TransactionRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO extends BaseDAO implements Repository<TransactionRecord, Integer> {

    @Override
    public TransactionRecord save(TransactionRecord t) throws DatabaseException {
        String sql = "INSERT INTO transactions (actor_type, actor_name, action, details) VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getActorType());
            ps.setString(2, t.getActorName());
            ps.setString(3, t.getAction());
            ps.setString(4, t.getDetails());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getInt(1));
            }
            return t;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public Optional<TransactionRecord> findById(Integer id) throws DatabaseException {
        List<TransactionRecord> list = query("SELECT * FROM transactions WHERE id = ?", id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<TransactionRecord> findAll() throws DatabaseException {
        return search(null, null, 500);
    }

    /** Overloaded convenience method (Week 4). */
    public List<TransactionRecord> latest(int limit) throws DatabaseException {
        return search(null, null, limit);
    }

    public List<TransactionRecord> search(String keyword, String actorType, int limit) throws DatabaseException {
        StringBuilder sql = new StringBuilder("SELECT * FROM transactions WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (actorType != null) { sql.append("AND actor_type = ? "); params.add(actorType); }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (actor_name LIKE ? OR action LIKE ? OR details LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            for (int i = 0; i < 3; i++) params.add(like);
        }
        sql.append("ORDER BY created_at DESC, id DESC LIMIT ").append(Math.max(1, limit));
        return query(sql.toString(), params.toArray());
    }

    public int count() throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM transactions");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private List<TransactionRecord> query(String sql, Object... params) throws DatabaseException {
        List<TransactionRecord> list = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TransactionRecord(rs.getInt("id"), rs.getString("actor_type"), rs.getString("actor_name"),
                            rs.getString("action"), rs.getString("details"), rs.getTimestamp("created_at").toLocalDateTime()));
                }
            }
            return list;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }
}
