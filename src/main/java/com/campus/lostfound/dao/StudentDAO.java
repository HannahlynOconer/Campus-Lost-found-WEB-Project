package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO extends BaseDAO implements Repository<Student, Integer> {

    @Override
    public Student save(Student s) throws DatabaseException {
        String sql = "INSERT INTO students (student_number, full_name) VALUES (?, ?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getStudentNumber());
            ps.setString(2, s.getFullName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getInt(1));
            }
            return s;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    @Override
    public Optional<Student> findById(Integer id) throws DatabaseException {
        return queryOne("SELECT * FROM students WHERE id = ?", String.valueOf(id));
    }

    public Optional<Student> findByNumber(String number) throws DatabaseException {
        return queryOne("SELECT * FROM students WHERE student_number = ?", number);
    }

    @Override
    public List<Student> findAll() throws DatabaseException {
        List<Student> list = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT * FROM students ORDER BY created_at DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    public int count() throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM students");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Optional<Student> queryOne(String sql, String param) throws DatabaseException {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw wrap(e);
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("id"), rs.getString("student_number"), rs.getString("full_name"));
    }
}
