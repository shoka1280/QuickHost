package com.Project.QuickHost.Controller;


import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.BookingRequest;
import com.Project.QuickHost.Dto.GuestDto;
import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.enums.BookingStatus;
import com.Project.QuickHost.Service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    //Booking controller
    @PostMapping("{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initiatePayement(@PathVariable Long bookingId)
    {
        String sessionUrl=bookService.intiatePayements(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl",sessionUrl));

    }

    @PostMapping("{bookingId}/cancel")
    public ResponseEntity<Void> intiate_Cancel (@PathVariable Long bookingId)
    {
        bookService.initiateCancel(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("{bookingId}/status")
    public ResponseEntity<Map<String,String>> getStatus (@PathVariable Long bookingId)
    {

        BookingStatus status=bookService.getBookingStatus(bookingId);
        return ResponseEntity.ok(Map.of("bookingStatus",status.toString()));
    }
}
