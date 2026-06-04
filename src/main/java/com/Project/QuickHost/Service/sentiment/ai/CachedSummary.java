package com.Project.QuickHost.Service.sentiment.ai;

import java.util.Map;

public record CachedSummary(
        SummaryResult summary,                 // narrative + pros + cons (from Gemini)
        Map<String, Double> aspectAverages,    // from aggregator
        double overallStars                    // from aggregator
) {}