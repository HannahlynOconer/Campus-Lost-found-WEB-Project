package com.campus.lostfound.model;

public enum ApprovalStatus {
    PENDING("Pending"), APPROVED("Approved"), REJECTED("Rejected");

    private final String label;
    ApprovalStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
    @Override public String toString() { return label; }
}
