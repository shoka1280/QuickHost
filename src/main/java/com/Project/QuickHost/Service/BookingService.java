package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookingService {
    public BookingDto addGuest(Long bookingId,List<GuestDto> guestList);
    public BookingDto initializeBooking(@RequestBody BookingRequest book);

}
