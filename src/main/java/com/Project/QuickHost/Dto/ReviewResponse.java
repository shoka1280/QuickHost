package com.Project.QuickHost.Dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id, Long hotelId, Long userId,
        String text, int rating, LocalDateTime createdAt,
//        Sentiment overallSentiment, Double sentimentScore, String snippet,
//        Map<String, Double> aspectScores,
//        AnalysisStatus analysisStatus, LocalDateTime analyzedAt
        LocalDateTime analyzedAt) {}