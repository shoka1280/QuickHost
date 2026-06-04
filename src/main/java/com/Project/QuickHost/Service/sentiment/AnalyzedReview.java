package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Entity.enums.Sentiment;

import java.util.Map;

public record AnalyzedReview(Sentiment sentiment, double score,
                             String snippet, Map<String, Double> aspects) {}