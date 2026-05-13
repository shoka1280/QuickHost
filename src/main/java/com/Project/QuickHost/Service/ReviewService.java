package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.CreateReviewRequest;
import com.Project.QuickHost.Dto.ReviewResponse;
import com.Project.QuickHost.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse createReview(Long hotelId, CreateReviewRequest req);
    Page<Review> getHotelReviews(Long hotelId, Pageable pageable);
    ReviewResponse getReview(Long id);
}
