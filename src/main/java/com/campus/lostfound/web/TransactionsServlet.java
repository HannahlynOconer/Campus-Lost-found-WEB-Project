package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.TransactionRecord;
import com.campus.lostfound.service.ReportService;
import com.campus.lostfound.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/** Shows every transaction; "?export=1" downloads the same list as a CSV file (file handling). */
@WebServlet("/admin/transactions")
public class TransactionsServlet extends BaseServlet {
    private final TransactionService transactions = new TransactionService();
    private final ReportService reports = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actor = param(req, "actor");
        String role = actor.equals("student") || actor.equals("admin") ? actor : null;
        List<TransactionRecord> list = Collections.emptyList();
        try {
            list = transactions.search(param(req, "q"), role);
        } catch (LostFoundException e) {
            flashNow(req, "Could not load transactions: " + e.getMessage(), "error");
        }

        if ("1".equals(req.getParameter("export"))) {
            if (!list.isEmpty()) {
                resp.setContentType("text/csv; charset=UTF-8");
                resp.setHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");
                reports.writeCsv(resp.getWriter(), list);
                return;
            }
            flashNow(req, "There is nothing to export.", "error");
        }
        req.setAttribute("transactions", list);
        render(req, resp, "transactions", "Transactions", "admin", "transactions");
    }
}
