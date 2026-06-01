package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Repository.ReviewRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class SentimentBatchWorker {
    @Value("${review.batch.size:5}") private int batchSize;
    private final ReviewRepo reviewRepo;
    private final SentimentAnalysisGraph graph;

    @Scheduled(fixedDelayString = "${review.batch.interval-ms:60000}")
    @Transactional   // jakarta.transaction.Transactional
    public void drainPending() {
        var batch = reviewRepo.lockNextBatchForAnalysis(
                AnalysisStatus.PENDING, LocalDateTime.now().minusMinutes(2), PageRequest.of(0, batchSize));
        if (batch.isEmpty()) return;
        log.info("Draining {} PENDING reviews", batch.size());
        batch.forEach(this::processOne);
    }

    @Scheduled(fixedDelayString = "${review.retry.interval-ms:300000}")
    @Transactional
    public void retryFailed() {
        reviewRepo.lockNextFailedForRetry(
                        AnalysisStatus.FAILED, 3, LocalDateTime.now(), PageRequest.of(0, batchSize))
                .forEach(this::processOne);
    }

    private void processOne(Review r) {
        try {
            var a = graph.run(r.getText());
            r.setOverallSentiment(a.sentiment()); r.setSentimentScore(a.score());
            r.setSnippet(a.snippet()); r.setAspectScores(a.aspects());
            r.setAnalysisStatus(AnalysisStatus.COMPLETED); r.setAnalyzedAt(LocalDateTime.now());
            r.setAnalysisError(null);
        } catch (Exception e) {
            r.setRetryCount(r.getRetryCount() + 1);
            String msg = e.getMessage();
            r.setAnalysisError(msg == null ? null : msg.substring(0, Math.min(msg.length(), 1000)));
            r.setAnalysisStatus(AnalysisStatus.FAILED); r.setAnalysisError(msg);
            r.setNextAttemptAt(LocalDateTime.now().plusMinutes((long) Math.pow(5, r.getRetryCount() - 1)));
        }
        reviewRepo.save(r);
    }
}