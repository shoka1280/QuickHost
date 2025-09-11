package com.Project.QuickHost.Controller;


import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import com.Project.QuickHost.Service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {
    private final BookingService bookService;
    //creating a new booking
    @PostMapping("/init")
        public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest book)
        {
            return ResponseEntity.ok(bookService.initializeBooking(book));

        }
        @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuest(@PathVariable Long bookingId, @RequestBody List<GuestDto>guestList)
        {
            return ResponseEntity.ok(bookService.addGuest(bookingId,guestList));
        }
}
