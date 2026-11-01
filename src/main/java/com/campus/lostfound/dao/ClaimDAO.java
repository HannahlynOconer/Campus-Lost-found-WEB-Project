package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Claim;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClaimDAO extends BaseDAO implements Repository<Claim, Integer> {
    private static final String SELECT =
            "SELECT c.*, i.item_name, s.full_name, s.student_number FROM claims c "
          + "JOIN items i ON i.id = c.item_id JOIN students s ON s.id = c.student_id ";

    @Override
    public Claim save(Claim claim) throws DatabaseException {
        String sql = "INSERT INTO claims (item_id, student_id, proof, status) VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, claim.getItemId());
            ps.setInt(2, claim.getStudentId());
            ps.setString(3, claim.getProof());
            ps.setString(4, claim.getApproval().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) claim.setId(keys.getInt(1));
            }
            return claim;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public Optional<Claim> findById(Integer id) throws DatabaseException {
        List<Claim> list = query(SELECT + "WHERE c.id = ?", id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Claim> findAll() throws DatabaseException {
        return query(SELECT + "ORDER BY FIELD(c.status,'PENDING','APPROVED','REJECTED'), c.created_at DESC");
    }

    public List<Claim> findByStudent(int studentId) throws DatabaseException {
        return query(SELECT + "WHERE c.student_id = ? ORDER BY c.created_at DESC", studentId);
    }

    public void updateStatus(int id, ApprovalStatus status) throws DatabaseException {
        update("UPDATE claims SET status = ? WHERE id = ?", status.name(), id);
    }

    public void rejectOtherPending(int itemId, int exceptClaimId) throws DatabaseException {
        update("UPDATE claims SET status = 'REJECTED' WHERE item_id = ? AND id <> ? AND status = 'PENDING'", itemId, exceptClaimId);
    }

    public boolean existsPending(int itemId, int studentId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM claims WHERE item_id = ? AND student_id = ? AND status = 'PENDING'";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    public int countByStatus(ApprovalStatus status) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM claims WHERE status = ?")) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private void update(String sql, Object... params) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private List<Claim> query(String sql, Object... params) throws DatabaseException {
        List<Claim> list = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Claim map(ResultSet rs) throws SQLException {
        Claim claim = new Claim(rs.getInt("id"), rs.getInt("item_id"), rs.getInt("student_id"), rs.getString("proof"),
                ApprovalStatus.valueOf(rs.getString("status")), rs.getTimestamp("created_at").toLocalDateTime());
        claim.setDisplayInfo(rs.getString("item_name"), rs.getString("full_name"), rs.getString("student_number"));
        return claim;
    }
}
