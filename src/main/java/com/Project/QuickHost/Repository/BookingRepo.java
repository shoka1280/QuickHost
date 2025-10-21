package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Bookings,Long> {
    Optional<Bookings> findByStripePaymentsessionId(String stripePaymentsessionId);
}
