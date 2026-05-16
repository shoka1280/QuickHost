package com.Project.QuickHost.Repository;


import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Entity.enums.AnalysisStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}