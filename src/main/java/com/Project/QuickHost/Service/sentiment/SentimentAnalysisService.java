package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Repository.ReviewRepo;
import com.Project.QuickHost.Service.sentiment.AnalyzedReview;
import com.Project.QuickHost.Service.sentiment.SentimentAnalysisGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SentimentAnalysisService {
    private final ReviewRepo reviewRepo;
    private final SentimentAnalysisGraph graph;
    //We use @Async and transaction because, we need this method to have own thread[since rest can executy without it and a new db transaction (@Transactional)
    @Async("sentimentExecutor")
    @Transactional
    public CompletableFuture<Void> analyzeAsync(Long reviewId) {
        Review r = reviewRepo.findById(reviewId).orElseThrow();
        try {
            log.info("running the graph");
            AnalyzedReview a = graph.run(r.getText());
            r.setOverallSentiment(a.sentiment());
            r.setSentimentScore(a.score());
            r.setSnippet(a.snippet());
            r.setAspectScores(a.aspects());
            r.setAnalysisStatus(AnalysisStatus.COMPLETED);
            r.setAnalyzedAt(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Analysis failed for review {}", reviewId, e);
            r.setAnalysisStatus(AnalysisStatus.FAILED);
            r.setAnalysisError(e.getMessage());
        }
        reviewRepo.save(r);
        return CompletableFuture.completedFuture(null);
    }
}