package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
