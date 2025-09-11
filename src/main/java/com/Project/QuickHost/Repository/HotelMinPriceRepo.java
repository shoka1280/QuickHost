package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Dto.HotelPriceDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepo extends JpaRepository<HotelMinPrice,Long> {
    @Query("""
      SELECT new com.Project.QuickHost.Dto.HotelPriceDto(i.hotel,AVG(i.price))
            FROM HotelMinPrice i
                   WHERE i.hotel.city=:city  
                         AND i.date BETWEEN :startDate AND :endDate
                         AND i.hotel.active=true
                        GROUP BY i.hotel
                             """)//distict baes on hotel androom

    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city")String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")LocalDate endDate,

            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
