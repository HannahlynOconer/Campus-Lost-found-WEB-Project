package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemType;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAO extends BaseDAO implements Repository<Item, Integer> {
    private static final String SELECT =
            "SELECT i.*, s.full_name AS reporter_name FROM items i JOIN students s ON s.id = i.student_id ";

    @Override
    public Item save(Item item) throws DatabaseException {
        String sql = "INSERT INTO items (student_id, item_name, category, description, location, item_date, item_type, approval, claimed) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getReporterId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getCategory());
            ps.setString(4, item.getDescription());
            ps.setString(5, item.getLocation());
            ps.setDate(6, Date.valueOf(item.getDateOccurred()));
            ps.setString(7, item.getType().name());
            ps.setString(8, item.getApproval().name());
            ps.setBoolean(9, item.isClaimed());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) item.setId(keys.getInt(1));
            }
            return item;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public Optional<Item> findById(Integer id) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(SELECT + "WHERE i.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public List<Item> findAll() throws DatabaseException {
        return find(null, null, null, null, null);
    }

    /** Flexible search: any argument that is null is ignored. */
    public List<Item> find(ApprovalStatus approval, Boolean claimed, String keyword, ItemType type, Integer reporterId)
            throws DatabaseException {
        StringBuilder sql = new StringBuilder(SELECT).append("WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (approval != null)   { sql.append("AND i.approval = ? ");  params.add(approval.name()); }
        if (claimed != null)    { sql.append("AND i.claimed = ? ");   params.add(claimed); }
        if (type != null)       { sql.append("AND i.item_type = ? "); params.add(type.name()); }
        if (reporterId != null) { sql.append("AND i.student_id = ? "); params.add(reporterId); }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (i.item_name LIKE ? OR i.category LIKE ? OR i.location LIKE ? OR i.description LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(like);
        }
        sql.append("ORDER BY i.created_at DESC");

        List<Item> items = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(map(rs));
            }
            return items;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    public void updateApproval(int id, ApprovalStatus status) throws DatabaseException {
        execute("UPDATE items SET approval = ? WHERE id = ?", status.name(), id);
    }

    public void markClaimed(int id) throws DatabaseException {
        execute("UPDATE items SET claimed = TRUE WHERE id = ?", id);
    }

    public boolean delete(int id) throws DatabaseException {
        return execute("DELETE FROM items WHERE id = ?", id) > 0;
    }

    public int count(ApprovalStatus approval, Boolean claimed) throws DatabaseException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM items WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (approval != null) { sql.append("AND approval = ? "); params.add(approval.name()); }
        if (claimed != null)  { sql.append("AND claimed = ? ");  params.add(claimed); }
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private int execute(String sql, Object... params) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Item map(ResultSet rs) throws SQLException {
        Item item = Item.create(
                ItemType.valueOf(rs.getString("item_type")),
                rs.getInt("id"), rs.getInt("student_id"),
                rs.getString("item_name"), rs.getString("category"), rs.getString("description"),
                rs.getString("location"), rs.getDate("item_date").toLocalDate(),
                ApprovalStatus.valueOf(rs.getString("approval")), rs.getBoolean("claimed"));
        item.setReporterName(rs.getString("reporter_name"));
        return item;
    }
}
