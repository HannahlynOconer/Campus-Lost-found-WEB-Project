package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.Claim;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import com.campus.lostfound.service.StudentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/students")
public class StudentsServlet extends BaseServlet {

    /** One table row for the JSP (plain getters so the JSP expression language can read it). */
    public static class StudentRow {
        private final Student student;
        private final int reports;
        private final int claims;

        public StudentRow(Student student, int reports, int claims) {
            this.student = student;
            this.reports = reports;
            this.claims = claims;
        }
        public String getStudentNumber() { return student.getStudentNumber(); }
        public String getFullName() { return student.getFullName(); }
        public int getReports() { return reports; }
        public int getClaims() { return claims; }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<StudentRow> rows = new ArrayList<>();
        try {
            Map<Integer, Integer> reportCount = new HashMap<>();
            for (Item item : new ItemService().getAll(null)) reportCount.merge(item.getReporterId(), 1, Integer::sum);
            Map<Integer, Integer> claimCount = new HashMap<>();
            for (Claim claim : new ClaimService().getAll()) claimCount.merge(claim.getStudentId(), 1, Integer::sum);
            for (Student s : new StudentService().getAll()) {
                rows.add(new StudentRow(s, reportCount.getOrDefault(s.getId(), 0), claimCount.getOrDefault(s.getId(), 0)));
            }
        } catch (LostFoundException e) {
            flashNow(req, "Could not load students: " + e.getMessage(), "error");
        }
        req.setAttribute("rows", rows);
        render(req, resp, "students", "Students", "admin", "students");
    }
}
