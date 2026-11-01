package com.campus.lostfound.web;

import com.campus.lostfound.model.User;
import com.campus.lostfound.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = currentStudent(req) != null ? currentStudent(req) : currentAdmin(req);   // polymorphism: both are Users
            auth.logout(user);
            session.invalidate();
        }
        redirect(req, resp, "/welcome");
    }
}
