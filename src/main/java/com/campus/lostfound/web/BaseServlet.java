package com.campus.lostfound.web;

import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * ABSTRACT parent of every servlet (inheritance + abstraction, Weeks 8-10).
 * It holds the helper methods that all controllers share.
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        super.service(req, resp);
    }

    /** Shows a JSP page (full-width layout). */
    protected void render(HttpServletRequest req, HttpServletResponse resp, String view, String title,
                          String role, String active) throws ServletException, IOException {
        render(req, resp, view, title, role, active, "wide");
    }

    /** Overloaded (Week 4): lets login pages use the narrow layout. */
    protected void render(HttpServletRequest req, HttpServletResponse resp, String view, String title,
                          String role, String active, String layout) throws ServletException, IOException {
        req.setAttribute("pageTitle", title);
        req.setAttribute("role", role);
        req.setAttribute("active", active);
        req.setAttribute("layout", layout);
        moveFlashToRequest(req);
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
    }

    /** Message that survives a redirect (stored in the session until the next page is shown). */
    protected void flash(HttpServletRequest req, String message, String type) {
        HttpSession session = req.getSession();
        session.setAttribute("flashMessage", message);
        session.setAttribute("flashType", type);
    }

    /** Message for the page being rendered right now. */
    protected void flashNow(HttpServletRequest req, String message, String type) {
        req.setAttribute("flashMessage", message);
        req.setAttribute("flashType", type);
    }

    private void moveFlashToRequest(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("flashMessage") != null && req.getAttribute("flashMessage") == null) {
            req.setAttribute("flashMessage", session.getAttribute("flashMessage"));
            req.setAttribute("flashType", session.getAttribute("flashType"));
        }
        if (session != null) {
            session.removeAttribute("flashMessage");
            session.removeAttribute("flashType");
        }
    }

    protected Student currentStudent(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (Student) session.getAttribute("student");
    }

    protected Admin currentAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (Admin) session.getAttribute("admin");
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    protected static String param(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value.trim();
    }

    protected static int intParam(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(param(req, name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected static LocalDate dateParam(HttpServletRequest req, String name) {
        try {
            return LocalDate.parse(param(req, name));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
