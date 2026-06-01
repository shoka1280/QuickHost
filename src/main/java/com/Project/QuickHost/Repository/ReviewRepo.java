package com.Project.QuickHost.Repository;


import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    Page<Review> findByHotel(Hotel hotel, Pageable pageable);

    Page<Review> findByHotel_Id(Long hotelId, Pageable pageable); //getting review by hotelId  review has hotel and hotel has hotelId that why er have _Id

    boolean existsByUserAndHotel(User user, Hotel hotel);

    long countByHotel_IdAndAnalysisStatus(Long hotelId, AnalysisStatus status);

    List<Review> findTop100ByHotel_IdAndAnalysisStatusOrderByCreatedAtDesc(
            Long hotelId, AnalysisStatus status);

    @Query("select avg(r.rating) from Review r where r.hotel.id = :hotelId")
    Double averageRating(@Param("hotelId") Long hotelId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2"))  // SKIP_LOCKED
    @Query("""
       select r from Review r
       where r.analysisStatus = :status and r.createdAt < :olderThan
       order by r.createdAt
       """)
    List<Review> lockNextBatchForAnalysis(@Param("status") AnalysisStatus status,
                                          @Param("olderThan")LocalDateTime olderThan,
                                          Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2"))
    @Query("""
     select r from Review  r 
     where r.analysisStatus=:status
      and r.retryCount<:maxRetries
      and (r.nextAttemptAt is null or r.nextAttemptAt<=:now)
      order by r.nextAttemptAt  nulls first 
""")
    List<Review>lockNextFailedForRetry(@Param("status") AnalysisStatus status,
                                       @Param("maxRetries") int maxRetries,
                                       @Param("now") LocalDateTime now, Pageable pageable);


}