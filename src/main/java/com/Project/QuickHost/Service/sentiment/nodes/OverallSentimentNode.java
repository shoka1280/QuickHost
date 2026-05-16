package com.Project.QuickHost.Service.sentiment.nodes;

import com.Project.QuickHost.Service.sentiment.OverallResult;
import com.Project.QuickHost.Service.sentiment.ai.OverallSentimentExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OverallSentimentNode {//Node 1
    private final OverallSentimentExtractor extractor;
    public OverallResult execute(String text) { return extractor.classify(text); }
}
