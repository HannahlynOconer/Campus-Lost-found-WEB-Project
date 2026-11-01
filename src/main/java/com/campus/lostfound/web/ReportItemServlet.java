package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.ValidationException;
import com.campus.lostfound.model.ItemType;
import com.campus.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/student/report")
public class ReportItemServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showForm(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ItemType type = parseType(param(req, "status"));
            items.reportItem(currentStudent(req), type, param(req, "itemName"), param(req, "category"),
                    param(req, "description"), param(req, "location"), dateParam(req, "itemDate"));
            flash(req, "Item submitted! It will appear in View Items once an admin approves it.", "success");
            redirect(req, resp, "/student/items");
        } catch (LostFoundException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp);
        }
    }

    private ItemType parseType(String text) {
        try {
            return ItemType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Please choose Lost or Found.");
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", ItemService.CATEGORIES);
        req.setAttribute("today", LocalDate.now().toString());
        render(req, resp, "report-item", "Report Item", "student", "report");
    }
}
