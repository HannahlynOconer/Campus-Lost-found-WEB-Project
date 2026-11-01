package com.campus.lostfound.service;

import com.campus.lostfound.dao.ClaimDAO;
import com.campus.lostfound.dao.ItemDAO;
import com.campus.lostfound.dao.StudentDAO;
import com.campus.lostfound.dao.TransactionDAO;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Exportable;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final ClaimDAO claimDAO = new ClaimDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    /** Dashboard numbers. LinkedHashMap keeps the insertion order (Week 14 Map). */
    public Map<String, Integer> getAdminStats() throws DatabaseException {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Students", studentDAO.count());
        stats.put("Total Items", itemDAO.count(null, null));
        stats.put("Pending Items", itemDAO.count(ApprovalStatus.PENDING, null));
        stats.put("Pending Claims", claimDAO.countByStatus(ApprovalStatus.PENDING));
        stats.put("Claimed Items", itemDAO.count(null, true));
        stats.put("Transactions", transactionDAO.count());
        return stats;
    }

    /**
     * FILE HANDLING + GENERICS: exports any list of Exportable objects (items, claims, transactions) to CSV.
     * "? extends Exportable" is a bounded wildcard.
     */
    public int exportCsv(Path file, List<? extends Exportable> rows) throws IOException, LostFoundException {
        if (rows.isEmpty()) throw new LostFoundException("There is nothing to export.");
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(String.join(",", rows.get(0).csvHeader()));
            writer.newLine();
            for (Exportable row : rows) {
                writer.write(row.toCsvRow());
                writer.newLine();
            }
        }
        return rows.size();
    }

    /** WEB version: writes the CSV to any Writer (e.g. the HTTP response). Caller makes sure the list is not empty. */
    public void writeCsv(Writer writer, List<? extends Exportable> rows) throws IOException {
        writer.write(String.join(",", rows.get(0).csvHeader()));
        writer.write("\r\n");
        for (Exportable row : rows) {
            writer.write(row.toCsvRow());
            writer.write("\r\n");
        }
        writer.flush();
    }
}
