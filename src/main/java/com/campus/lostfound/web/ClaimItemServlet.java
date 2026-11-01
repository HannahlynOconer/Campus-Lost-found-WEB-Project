package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/student/claim")
public class ClaimItemServlet extends BaseServlet {
    private final ItemService items = new ItemService();
    private final ClaimService claims = new ClaimService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showForm(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            claims.submitClaim(currentStudent(req), intParam(req, "itemId"), param(req, "proof"));
            flash(req, "Claim submitted! Wait for admin approval.", "success");
            redirect(req, resp, "/student/claim");
        } catch (LostFoundException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("available", items.getAvailableForClaim());
            req.setAttribute("myClaims", claims.getByStudent(currentStudent(req)));
        } catch (LostFoundException e) {
            flashNow(req, "Could not load data: " + e.getMessage(), "error");
        }
        req.setAttribute("selectedItemId", intParam(req, "itemId"));
        render(req, resp, "claim-item", "Claim Item", "student", "claim");
    }
}
