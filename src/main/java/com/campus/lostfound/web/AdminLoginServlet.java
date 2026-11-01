package com.campus.lostfound.web;

import com.campus.lostfound.exception.AuthenticationException;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Admin login: username + PASSWORD. */
@WebServlet("/admin/login")
public class AdminLoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentAdmin(req) != null) {
            redirect(req, resp, "/admin/home");
            return;
        }
        render(req, resp, "admin-login", "Admin Login", null, "", "narrow");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            auth.ensureDefaultAdmin();                      // in case MySQL was down when Tomcat started
            Admin admin = auth.loginAdmin(param(req, "username"), req.getParameter("password"));
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.setAttribute("admin", admin);
            redirect(req, resp, "/admin/home");
        } catch (AuthenticationException | DatabaseException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            render(req, resp, "admin-login", "Admin Login", null, "", "narrow");
        }
    }
}
