package com.Project.QuickHost.Service.impl;

import com.Project.QuickHost.Dto.CreateReviewRequest;
import com.Project.QuickHost.Dto.ReviewResponse;
import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Repository.BookingRepo;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.ReviewRepo;
import com.Project.QuickHost.Service.ReviewService;
import com.Project.QuickHost.Util.AppUtils;
import com.Project.QuickHost.exception.ConflictException;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import com.Project.QuickHost.exception.UnAuthorisedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepo reviewRepo;
    private final HotelRepo hotelRepo;
    private final BookingRepo bookingRepo;
    // private final SentimentAnalysisService sentimentAnalysisService; // added in Phase 3

    @Override
    @Transactional
    public ReviewResponse createReview(Long hotelId, CreateReviewRequest req) {
        User user = AppUtils.getCurrentUser();
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel " + hotelId));


//        Bookings stay = bookingRepo.mostRecentCompletedStay(user, hotel)
//                .orElseThrow(() -> new UnAuthorisedException("Complete a stay before reviewing."));


        if (reviewRepo.existsByUserAndHotel(user, hotel))
            throw new ConflictException("You have already reviewed this hotel.");

        Review r = new Review();
        r.setUser(user);
        r.setHotel(hotel);
//        r.setBooking(stay);
        r.setText(req.text());
        r.setRating(req.rating());
        r.setAnalysisStatus(AnalysisStatus.PENDING);
        Review saved = reviewRepo.save(r);

        // Phase 3 inserts: sentimentAnalysisService.analyzeAsync(saved.getId());
        return toDto(saved);
    }

    @Override
    public Page<Review> getHotelReviews(Long hotelId, Pageable pageable) {
        return reviewRepo.findByHotel_Id(hotelId, pageable);
    }

    @Override
    public ReviewResponse getReview(Long id) {
        return toDto(reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review " + id)));
    }

    private ReviewResponse toDto(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getHotel().getId(),
                r.getUser().getId(),
                r.getText(),
                r.getRating(),
                r.getCreatedAt(),
//                r.getOverallSentiment(),
//                r.getSentimentScore(),
//                r.getSnippet(),
//                r.getAspectScores(),
//                r.getAnalysisStatus(),
                r.getAnalyzedAt()
        );
    }
}
