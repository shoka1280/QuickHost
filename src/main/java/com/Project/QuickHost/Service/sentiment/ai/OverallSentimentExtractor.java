package com.Project.QuickHost.Service.sentiment.ai;

import com.Project.QuickHost.Service.sentiment.OverallResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
// p3
public interface OverallSentimentExtractor {//Node 1
    @SystemMessage("""
        You are a hotel-review sentiment classifier. Read the review and return JSON only.
        Fields:
          sentiment: POSITIVE | NEUTRAL | NEGATIVE
          score:     decimal in [-1.0, 1.0] (-1 very negative, 1 very positive)
          snippet:   single sentence, max 200 chars, summarising the review
        """)
    @UserMessage("Review: {{text}}")
    OverallResult classify(@V("text") String text); // Classify the internship , code of modularity, of message
}



//   1- @SystemMessage(...) becomes the LLM's system turn — its rules. Same for every call.
//   2- @UserMessage("Review: {{text}}") becomes the user turn. {{text}} is a template placeholder.
//   3- @V("text") String text is the value that fills {{text}}. The @V name and the template name must match.
//    4- OverallResult classify(...) — the return type tells LangChain4j what JSON schema to ask Gemini for. It then parses the JSON into a record. You write zero JSON parsing code.

