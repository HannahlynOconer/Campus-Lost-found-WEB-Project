package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.ItemType;
import com.campus.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/student/items")
public class ViewItemsServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeText = param(req, "type");
        ItemType type = null;
        if (typeText.equals("LOST")) type = ItemType.LOST;
        else if (typeText.equals("FOUND")) type = ItemType.FOUND;
        try {
            req.setAttribute("items", items.search(param(req, "q"), type));      // overloaded search(String, ItemType)
            req.setAttribute("mine", items.getByReporter(currentStudent(req)));
        } catch (LostFoundException e) {
            flashNow(req, "Could not load items: " + e.getMessage(), "error");
        }
        render(req, resp, "view-items", "View Items", "student", "view");
    }
}
