package com.Project.QuickHost.Service.sentiment.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface HotelSummarizer {
    @SystemMessage("""
        You are summarising hotel reviews for prospective guests.
        Given aggregate stats and sample snippets, produce JSON only with:
          narrative: 2-3 sentences, neutral and factual
          pros:      array of exactly 3 strings (≤80 chars each)
          cons:      array of exactly 3 strings (≤80 chars each)
        Base pros on positive snippets and high aspect averages.
        Base cons on negative snippets and low aspect averages.
        Do not invent specifics not present in the input.
        """)
    @UserMessage("""
        Aspect averages (-1 to 1): {{aspects}}
        Sentiment distribution: {{dist}}
        Positive snippets: {{pos}}
        Negative snippets: {{neg}}
        """)
    SummaryResult summarise(@V("aspects") String aspects, @V("dist") String dist,
                            @V("pos") String pos, @V("neg") String neg);
}
