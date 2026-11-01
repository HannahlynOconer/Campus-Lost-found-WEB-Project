package com.campus.lostfound.web;

import com.campus.lostfound.exception.AuthenticationException;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Student login: student number + full name, NO password. */
@WebServlet("/student/login")
public class StudentLoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentStudent(req) != null) {
            redirect(req, resp, "/student/home");
            return;
        }
        render(req, resp, "student-login", "Student Login", null, "", "narrow");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Student student = auth.loginStudent(param(req, "studentNumber"), param(req, "fullName"));
            HttpSession session = req.getSession(true);
            req.changeSessionId();                         // protects against session fixation
            session.setAttribute("student", student);
            redirect(req, resp, "/student/home");
        } catch (AuthenticationException | DatabaseException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            render(req, resp, "student-login", "Student Login", null, "", "narrow");
        }
    }
}
