package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Entity.enums.Sentiment;

import java.time.LocalDateTime;
import java.util.Map;

public record ReviewResponse(
        Long id, Long hotelId, Long userId,
        String text, int rating, LocalDateTime createdAt,
        Sentiment overallSentiment, Double sentimentScore, String snippet,
        Map<String, Double> aspectScores,
        AnalysisStatus analysisStatus,
        LocalDateTime analyzedAt) {
    public static ReviewResponse from(Review r) {
        return new ReviewResponse(
                r.getId(), r.getHotel().getId(), r.getUser().getId(),
                r.getText(), r.getRating(), r.getCreatedAt(),
                r.getOverallSentiment(), r.getSentimentScore(), r.getSnippet(),
                r.getAspectScores(), r.getAnalysisStatus(), r.getAnalyzedAt());
    }
}