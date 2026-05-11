package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.ProfileUpdateRequestDto;
import com.Project.QuickHost.Dto.UserDto;
import com.Project.QuickHost.Service.BookingService;
import com.Project.QuickHost.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookingService bookingService;
    @PutMapping("/profile/update")
    public ResponseEntity<Void>updateUserProfile(@RequestBody ProfileUpdateRequestDto profileUpdate)
    {
        userService.profileUpdate(profileUpdate);
        // Implementation for updating user profile goes here

        return ResponseEntity.noContent().build();
    }
    @GetMapping ("/mybookings")

    public ResponseEntity<List<BookingDto>> getUserBookings()
    {
        // Implementation for fetching user bookings goes here

        return ResponseEntity.ok(bookingService.getAllBookingsByUser());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile()
    {
        return ResponseEntity.ok(userService.getUserProfile());
    }

}
