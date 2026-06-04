package com.Project.QuickHost.Service.sentiment;

import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Entity.enums.Sentiment;
import com.Project.QuickHost.Repository.ReviewRepo;
import com.Project.QuickHost.Service.HotelAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class HotelReviewAggregator {
    private final ReviewRepo reviewRepo;

    public HotelAggregate aggregate(Long hotelId) {
        double avgStars = Optional.ofNullable(reviewRepo.averageRating(hotelId)).orElse(0.0);
        List<Review> recent = reviewRepo
                .findTop100ByHotel_IdAndAnalysisStatusOrderByCreatedAtDesc(hotelId, AnalysisStatus.COMPLETED);//Reviews that went through all nodes.
        long count = reviewRepo.countByHotel_IdAndAnalysisStatus(hotelId, AnalysisStatus.COMPLETED);

        Map<String, Double> aspectAvg = averageAspects(recent);

        List<String> pos = recent.stream()  //top 8 highest sentiment score
                .filter(r -> r.getSentimentScore() != null && r.getSnippet() != null)
                .sorted(Comparator.comparingDouble(Review::getSentimentScore).reversed())
                .limit(8).map(Review::getSnippet).toList(); //extracting its snippet

        //lowest 8 highest sentiment score extracting it snippet,
        List<String> neg = recent.stream()//lowest 8 highest sentiment score
                .filter(r -> r.getSentimentScore() != null && r.getSnippet() != null)
                .sorted(Comparator.comparingDouble(Review::getSentimentScore))
                .limit(8).map(Review::getSnippet).toList(); //extraction reviews snippet only

//        example:       topPositiveSnippets = ["Best stay ever...", "Spotless rooms...", ..., "Comfortable beds..."]  // 8 strings
//            topNegativeSnippets = ["Filthy, loud...", "Overpriced...", ..., "Disappointing service..."]   // 8 strings

        Map<Sentiment, Long> dist = recent.stream()//Most recent sentiment,Distruction
                .filter(r -> r.getOverallSentiment() != null)
                .collect(Collectors.groupingBy(Review::getOverallSentiment, Collectors.counting()));//Overalall sentiment->Postive,Negative,Neutral

        double avgLlm = recent.stream() //Avg llm sentiment score.
                .filter(r -> r.getSentimentScore() != null)
                .mapToDouble(Review::getSentimentScore).average().orElse(0);
        //  Final hotel stars = 60% user star rating + 40% LLM sentiment score. So a 5-star hotel with negative LLM sentiment gets pulled down.
        double blendNorm = 0.6 * ((avgStars - 3) / 2) + 0.4 * avgLlm;     // -1..1
        double overallStars = Math.max(1, Math.min(5, (blendNorm + 1) * 2 + 1)); //Overall stars

        return new HotelAggregate(count, overallStars, aspectAvg, pos, neg, dist);//dist->Distance over here
    }

    //When is say aspect->we are talking about specific dimensions of reviewL:cleaniness,loctiom

    //Average score it took
    //  // bucket = { "cleanliness": [0.8, -0.2, 0.9, ...], "service": [...], ... }
    //  // then averages each list → { "cleanliness": 0.65, "service": 0.4, ... }
    Map<String, Double> averageAspects(List<Review> reviews) {//feeding top 100 reviews to  avgAspect
        Map<String, List<Double>> bucket = new HashMap<>();
        for (Review r : reviews) {
            if (r.getAspectScores() == null) continue;
            r.getAspectScores().forEach((k, v) ->
                    bucket.computeIfAbsent(k, x -> new ArrayList<>()).add(v));
        }
        return bucket.entrySet().stream().collect(toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)));
    }
}