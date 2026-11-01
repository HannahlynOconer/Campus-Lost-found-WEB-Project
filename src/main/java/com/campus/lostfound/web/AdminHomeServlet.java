package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.service.ReportService;
import com.campus.lostfound.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/home")
public class AdminHomeServlet extends BaseServlet {
    private final ReportService reports = new ReportService();
    private final TransactionService transactions = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("stats", reports.getAdminStats());          // Map<String,Integer>
            req.setAttribute("recent", transactions.latest(8));
        } catch (LostFoundException e) {
            flashNow(req, "Could not load dashboard: " + e.getMessage(), "error");
        }
        render(req, resp, "admin-home", "Admin Home", "admin", "home");
    }
}
