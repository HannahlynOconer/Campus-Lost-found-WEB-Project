package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/home")
public class StudentHomeServlet extends BaseServlet {
    private final ItemService items = new ItemService();
    private final ClaimService claims = new ClaimService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Student student = currentStudent(req);
        try {
            List<Item> mine = items.getByReporter(student);
            int pending = 0;
            for (Item item : mine) {
                if (item.isPending()) pending++;
            }
            List<Item> recent = items.search("");
            req.setAttribute("myReports", mine.size());
            req.setAttribute("pendingCount", pending);
            req.setAttribute("myClaims", claims.getByStudent(student).size());
            req.setAttribute("recent", recent.subList(0, Math.min(5, recent.size())));
        } catch (LostFoundException e) {
            flashNow(req, "Could not load data: " + e.getMessage(), "error");
        }
        render(req, resp, "student-home", "Home", "student", "home");
    }
}
