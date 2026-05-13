package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Bookings,Long> {
    Optional<Bookings> findByStripePaymentsessionId(String stripePaymentsessionId);

//    List<Bookings> getAllBookingByHotelId(Long hotelId);

    List<Bookings> getAllBookingByHotel(Hotel hotel);

    List<Bookings> getAllBookingByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDate, LocalDateTime endDate);

    List<Bookings> findByUser(User user);

    @Query("""
       select b from Bookings b
       where b.user = :user
         and b.hotel = :hotel
         and b.bookingStatus = com.Project.QuickHost.Entity.enums.BookingStatus.CONFIRMED
         and b.checkOutDate < CURRENT_DATE
       order by b.checkOutDate desc
       """)
    List<Bookings> findCompletedStaysOrdered(@Param("user") User user,
                                             @Param("hotel") Hotel hotel,
                                             Pageable pageable);

    default Optional<Bookings> mostRecentCompletedStay(User user, Hotel hotel) {
        return findCompletedStaysOrdered(user, hotel, PageRequest.of(0, 1))
                .stream().findFirst();
    }

}
