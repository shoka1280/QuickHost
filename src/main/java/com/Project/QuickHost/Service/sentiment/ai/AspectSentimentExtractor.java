package com.Project.QuickHost.Service.sentiment.ai;

import com.Project.QuickHost.Service.sentiment.AspectResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface AspectSentimentExtractor { //Node 2
    @SystemMessage("""
            Rate the hotel review on five aspects, each as a decimal in [0, 5.0]:
            cleanliness, service, location, value, room.
            If an aspect isn't mentioned, return 0.0. or you are open to give value base on understanding
            Return JSON only.
            """)
    @UserMessage("Review: {{text}}")
    AspectResult rate(@V("text") String text);
}
