package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.ReviewRepo;
import com.Project.QuickHost.Service.sentiment.HotelReviewAggregator;
import com.Project.QuickHost.Service.sentiment.ai.HotelSummarizer;
import com.Project.QuickHost.Service.sentiment.ai.SummaryResult;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Project.QuickHost.Dto.HotelReviewSummaryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j

public class HotelReviewSummaryService {
    private static final int threshold=10;//ENough to make first summart or enough for updated summary
    private final HotelRepo hotelRepo;
    private final ReviewRepo reviewRepo;
    private final HotelReviewAggregator hotelReviewAggregator;
    private final HotelSummarizer hotelSummarizer;
    private final ModelMapper modelMapper;//For internal obj to obj transafromtation
    private final ObjectMapper json;//For json seriliasation and deserilization
     @Transactional
    public HotelReviewSummaryResponse getOrGenerate(Long hotelId)
    {
        Hotel h=hotelRepo.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("HotelId not found:"+hotelId));

        long currentCount=reviewRepo.countByHotel_IdAndAnalysisStatus(hotelId, AnalysisStatus.COMPLETED);//count of reiew count anaylsis done,becuase it has pendind feild

        if (currentCount < threshold) {
            return new HotelReviewSummaryResponse(
                    hotelId,
                    "Not enough reviews yet to generate a summary.",
                    List.of(),       // empty pros
                    List.of(),       // empty cons
                    0.0,             // no overall rating
                    (int) currentCount,
                    Map.of(),        // no aspect averages
                    null             // never generated
            );
        }

        long lastCount=h.getReviewSummaryReviewCount() == null ? 0 : h.getReviewSummaryReviewCount();
        //reads the snapshot of how many reviews existed when the cache was last written. In-memory off the loaded Hotel entity


        if (h.getReviewSummary() != null &&
                currentCount - lastCount < threshold) {
            return deserializeCached(h, hotelId, currentCount);
        }

        HotelAggregate agg = hotelReviewAggregator.aggregate(hotelId);

        SummaryResult s;

        try{
            s=hotelSummarizer.summarise(
                    json.writeValueAsString(agg.aspectAverages()),
//                    Aspect averages (-1 to 1): {"cleanliness": 0.65, "service": -0.20, ...}
                    json.writeValueAsString(agg.distribution()),
                    // {"POSITIVE":62,"NEUTRAL":18,"NEGATIVE":20} helping to desided overal review where to keep postive,neutral,or negative,we are using equl snippet
                    String.join(" | ", agg.topPositiveSnippets()),
                    String.join(" | ",agg.topNegativeSnippets())
//             example:       topPositiveSnippets = ["Best stay ever...", "Spotless rooms...", ..., "Comfortable beds..."]  // 8 strings
//            topNegativeSnippets = ["Filthy, loud...", "Overpriced...", ..., "Disappointing service..."]   // 8 strings

            );

        }
         catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to (de)serialize summary payload",e);
        }

        try {
            h.setReviewSummary(json.writeValueAsString(s));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise summary for caching", e);
        }

        h.setReviewSummaryGeneratedAt(LocalDateTime.now());
        h.setReviewSummaryReviewCount((int) currentCount);
        hotelRepo.save(h);

        return new HotelReviewSummaryResponse(hotelId, s.narrative(), s.pros(), s.cons(),
                agg.overallStars(), (int) currentCount, agg.aspectAverages(),
                h.getReviewSummaryGeneratedAt());
    }

    private HotelReviewSummaryResponse deserializeCached(Hotel h, Long hotelId, long currentCount) {
        try {
            SummaryResult s = json.readValue(h.getReviewSummary(), SummaryResult.class);
            HotelAggregate agg = hotelReviewAggregator.aggregate(hotelId);  // for aspect avgs + overallStars
            return new HotelReviewSummaryResponse(hotelId, s.narrative(), s.pros(), s.cons(),
                    agg.overallStars(), (int) currentCount, agg.aspectAverages(),
                    h.getReviewSummaryGeneratedAt());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cached summary unparseable for hotel " + hotelId, e);
        }
    }


}



