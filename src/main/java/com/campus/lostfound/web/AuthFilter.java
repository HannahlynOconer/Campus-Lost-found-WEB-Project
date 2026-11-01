package com.campus.lostfound.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Keeps visitors out of /student/* and /admin/* pages until they have logged in. */
@WebFilter(urlPatterns = {"/student/*", "/admin/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (path.startsWith("/student/") && !path.equals("/student/login")
                && (session == null || session.getAttribute("student") == null)) {
            resp.sendRedirect(req.getContextPath() + "/student/login");
            return;
        }
        if (path.startsWith("/admin/") && !path.equals("/admin/login")
                && (session == null || session.getAttribute("admin") == null)) {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }
        resp.setHeader("Cache-Control", "no-store");   // the Back button must not show private pages after logout
        chain.doFilter(request, response);
    }
}
