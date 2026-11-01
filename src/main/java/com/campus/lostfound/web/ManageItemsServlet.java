package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.ValidationException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import com.campus.lostfound.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/** The administrator approves, rejects or deletes reported items here. */
@WebServlet("/admin/items")
public class ManageItemsServlet extends BaseServlet {
    private final ItemService items = new ItemService();
    private final ReportService reports = new ReportService();

    private static String cleanFilter(String filter) {
        return filter.equals("Pending") || filter.equals("Approved") || filter.equals("Rejected") ? filter : "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filter = cleanFilter(param(req, "filter"));
        List<Item> list = Collections.emptyList();
        try {
            ApprovalStatus status = filter.isEmpty() ? null : ApprovalStatus.valueOf(filter.toUpperCase());
            list = items.getAll(status);
        } catch (LostFoundException e) {
            flashNow(req, "Could not load items: " + e.getMessage(), "error");
        }

        if ("1".equals(req.getParameter("export")) && !list.isEmpty()) {
            resp.setContentType("text/csv; charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"items.csv\"");
            reports.writeCsv(resp.getWriter(), list);
            return;
        }
        req.setAttribute("items", list);
        req.setAttribute("filter", filter);
        render(req, resp, "manage-items", "Approve / Reject Items", "admin", "items");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin admin = currentAdmin(req);
        int id = intParam(req, "id");
        String filter = cleanFilter(param(req, "filter"));
        try {
            switch (param(req, "action")) {
                case "approve": items.approve(admin, id); flash(req, "Item approved.", "success"); break;
                case "reject":  items.reject(admin, id);  flash(req, "Item rejected.", "success"); break;
                case "delete":  items.delete(admin, id);  flash(req, "Item deleted.", "success"); break;
                default: throw new ValidationException("Unknown action.");
            }
        } catch (LostFoundException | IllegalArgumentException e) {
            flash(req, e.getMessage(), "error");
        }
        redirect(req, resp, "/admin/items" + (filter.isEmpty() ? "" : "?filter=" + filter));
    }
}
