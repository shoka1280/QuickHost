package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Entity.enums.BookingStatus;
import com.stripe.model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookingService {
    public BookingDto addGuest(Long bookingId,List<GuestDto> guestList);
    public BookingDto initializeBooking(@RequestBody BookingRequest book);

    public String intiatePayements(Long bookingId);

    void capturePayment(Event event);

    void initiateCancel(Long bookingId);

    BookingStatus getBookingStatus(Long bookingId);


    List<BookingDto> getAllBookingsByUser();
}
