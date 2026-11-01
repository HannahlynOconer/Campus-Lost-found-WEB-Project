package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.ValidationException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.service.ClaimService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/claims")
public class ManageClaimsServlet extends BaseServlet {
    private final ClaimService claims = new ClaimService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("claims", claims.getAll());
        } catch (LostFoundException e) {
            flashNow(req, "Could not load claims: " + e.getMessage(), "error");
        }
        render(req, resp, "manage-claims", "Claims", "admin", "claims");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin admin = currentAdmin(req);
        int id = intParam(req, "id");
        try {
            switch (param(req, "action")) {
                case "approve": claims.approve(admin, id); flash(req, "Claim approved.", "success"); break;
                case "reject":  claims.reject(admin, id);  flash(req, "Claim rejected.", "success"); break;
                default: throw new ValidationException("Unknown action.");
            }
        } catch (LostFoundException | IllegalArgumentException e) {
            flash(req, e.getMessage(), "error");
        }
        redirect(req, resp, "/admin/claims");
    }
}
