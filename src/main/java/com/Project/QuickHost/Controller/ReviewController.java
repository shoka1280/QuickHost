package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.CreateReviewRequest;
import com.Project.QuickHost.Dto.HotelReviewSummaryResponse;
import com.Project.QuickHost.Dto.PageModel;
import com.Project.QuickHost.Dto.ReviewResponse;
import com.Project.QuickHost.Dto.ReviewResponse.*;
import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Service.HotelReviewSummaryService;
import com.Project.QuickHost.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
     private final HotelReviewSummaryService summaryService; // Phase 4

    @PostMapping("/hotels/{hotelId}/reviews")
    public ResponseEntity<ReviewResponse> create(@PathVariable Long hotelId,
                                                 @Valid @RequestBody CreateReviewRequest req) {
//        log.info("rating: "+req.rating());
//        log.info("test"+req.text());
        return new ResponseEntity<>(reviewService.createReview(hotelId, req), HttpStatus.CREATED);
    }

    @GetMapping("/hotels/{hotelId}/reviews")
    public ResponseEntity<PageModel<ReviewResponse>> list(
            @PathVariable Long hotelId,
            @PageableDefault(size = 20, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> page = reviewService.getHotelReviews(hotelId, pageable);
        List<ReviewResponse> content = page.getContent().stream().map(ReviewResponse::from).toList();
        PageModel<ReviewResponse> body = new PageModel<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @GetMapping("/hotels/{hotelId}/review-summary")
    public ResponseEntity<HotelReviewSummaryResponse> summary(@PathVariable Long hotelId) {
        return ResponseEntity.ok(summaryService.getOrGenerate(hotelId));
    }


}
