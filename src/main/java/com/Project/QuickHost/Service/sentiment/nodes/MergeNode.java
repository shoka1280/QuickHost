package com.Project.QuickHost.Service.sentiment.nodes;

import com.Project.QuickHost.Service.sentiment.AspectResult;
import com.Project.QuickHost.Service.sentiment.AnalyzedReview;
import com.Project.QuickHost.Service.sentiment.OverallResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MergeNode {//Node 3
//        log.info("Merging nodes ");
    public AnalyzedReview merge(OverallResult o, AspectResult a) {
        return new AnalyzedReview(o.sentiment(), o.score(), o.snippet(), a.asMap());
    }
}