package com.Project.QuickHost.Service.sentiment.nodes;

import com.Project.QuickHost.Service.sentiment.AspectResult;
import com.Project.QuickHost.Service.sentiment.ai.AspectSentimentExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AspectSentimentNode {//Node 2
    private final AspectSentimentExtractor extractor;
    public AspectResult execute(String text) { return extractor.rate(text); }
}
