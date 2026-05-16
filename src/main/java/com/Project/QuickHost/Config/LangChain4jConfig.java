package com.Project.QuickHost.Config;

import com.Project.QuickHost.Service.sentiment.ai.OverallSentimentExtractor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {
    @Bean
    public ChatLanguageModel geminiModel(
            @Value("${gemini.api.key}") String key,
            @Value("${gemini.model}") String name,
            @Value("${gemini.temperature}") double temp) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(key).modelName(name).temperature(temp)
                .responseFormat(ResponseFormat.JSON)
                .build();
    }

    @Bean
    OverallSentimentExtractor overall(ChatLanguageModel m) {
        return AiServices.create(OverallSentimentExtractor.class, m);
    }
}