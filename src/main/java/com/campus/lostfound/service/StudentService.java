package com.campus.lostfound.service;

import com.campus.lostfound.dao.StudentDAO;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Student;
import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public List<Student> getAll() throws DatabaseException { return dao.findAll(); }
}
