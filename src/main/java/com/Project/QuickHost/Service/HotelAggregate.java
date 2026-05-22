package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.enums.Sentiment;

import java.util.List;
import java.util.Map;

public record HotelAggregate(
        long reviewCount,
        double overallStars,
        Map<String, Double> aspectAverages,
        List<String> topPositiveSnippets,
        List<String> topNegativeSnippets,
        Map<Sentiment, Long> distribution
) {}