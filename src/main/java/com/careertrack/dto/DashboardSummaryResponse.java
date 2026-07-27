package com.careertrack.dto;

public record DashboardSummaryResponse(
        long totalApplications,
        long wishlist,
        long applied,
        long assessment,
        long interview,
        long offered,
        long rejected,
        long withdrawn
) {
}
