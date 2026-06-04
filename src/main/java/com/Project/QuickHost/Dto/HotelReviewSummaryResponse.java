package com.Project.QuickHost.Dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record HotelReviewSummaryResponse(
        Long hotelId,
        String narrative,
        List<String> pros,
        List<String> cons,
        double overallRating,
        int reviewCount,
        Map<String, Double> aspectAverages,
        LocalDateTime generatedAt
) {}
