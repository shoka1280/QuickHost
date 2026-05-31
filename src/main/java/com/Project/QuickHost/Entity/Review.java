package com.Project.QuickHost.Entity;

import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import com.Project.QuickHost.Entity.enums.Sentiment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "Reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hotel_id"}))
public class Review {
    //logic one review per user per hotel.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //Many review for one hotel
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY) //Many reviews by one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Bookings booking;          // the specific stay being reviewed

    @Column(length = 4000, nullable = false)
    private String text;

    @Column(nullable = false)
    private int rating;                // user's 1..5 stars

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Phase 3 populates these — null until then
    @Enumerated(EnumType.STRING)
    private Sentiment overallSentiment;

    private Double sentimentScore;     // -1.0..1.0

    @Column(length = 280)
    private String snippet;

    @ElementCollection //Instead of packing multiple values into a single database row, @ElementCollection automatically creates a separate database table to store the elements
    @CollectionTable(name = "review_aspect_scores",//because it has multiple column
            joinColumns = @JoinColumn(name = "review_id"))
    @MapKeyColumn(name = "aspect")
    @Column(name = "score")
    private Map<String, Double> aspectScores;

    @Enumerated(EnumType.STRING)
    private AnalysisStatus analysisStatus;

    @Column(length = 1000)
    private String analysisError;

    private LocalDateTime analyzedAt;
}