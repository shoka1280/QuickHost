package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Service.sentiment.nodes.AspectSentimentNode;
import com.Project.QuickHost.Service.sentiment.nodes.MergeNode;
import com.Project.QuickHost.Service.sentiment.nodes.OverallSentimentNode;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class SentimentAnalysisGraph {
    //The graph orchestrator
    private final OverallSentimentNode overallNode;
    private final AspectSentimentNode aspectNode;
    private final MergeNode mergeNode;
    private final RateLimiter rateLimiter;
    @Qualifier("graphExecutor") private final Executor graphExecutor;
    //wrapping rate limit per review analysis
    public AnalyzedReview run(String reviewText) {
        var overallFut = CompletableFuture.supplyAsync(
                () -> rateLimiter.executeSupplier(() -> overallNode.execute(reviewText)), graphExecutor);
        var aspectFut  = CompletableFuture.supplyAsync(
                () -> rateLimiter.executeSupplier(() -> aspectNode.execute(reviewText)),  graphExecutor);
        return mergeNode.merge(overallFut.join(), aspectFut.join());
    }
}