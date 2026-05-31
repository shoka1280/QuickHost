package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Service.sentiment.nodes.AspectSentimentNode;
import com.Project.QuickHost.Service.sentiment.nodes.MergeNode;
import com.Project.QuickHost.Service.sentiment.nodes.OverallSentimentNode;
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
    @Qualifier("graphExecutor") private final Executor graphExecutor;

    public AnalyzedReview run(String reviewText) {
        var overallFut = CompletableFuture.supplyAsync(() -> overallNode.execute(reviewText), graphExecutor);
        log.info("overall"+overallFut);
        var aspectFut  = CompletableFuture.supplyAsync(() -> aspectNode.execute(reviewText),  graphExecutor);
        //Mergin both nodes of graph
        return mergeNode.merge(overallFut.join(), aspectFut.join());
    }
}