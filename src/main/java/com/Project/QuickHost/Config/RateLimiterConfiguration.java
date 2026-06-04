package com.Project.QuickHost.Config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
@Configuration
public class RateLimiterConfiguration {
    @Bean
    public RateLimiter geminiRateLimiter(@Value("${gemini.ratelimit.per-minute:10}") int perMinute) {
        var config = RateLimiterConfig.custom()
                .limitForPeriod(perMinute)                  // calls allowed per window
                .limitRefreshPeriod(Duration.ofMinutes(1))  // window = 1 minute
                .timeoutDuration(Duration.ofSeconds(30))    // wait up to 30s for a permit
                .build();
        return RateLimiter.of("gemini", config);
    }
}