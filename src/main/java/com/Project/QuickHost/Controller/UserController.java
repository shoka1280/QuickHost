package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.ProfileUpdateRequestDto;
import com.Project.QuickHost.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PutMapping("/profile/update")
    public ResponseEntity<Void>updateUserProfile(@RequestBody ProfileUpdateRequestDto profileUpdate)
    {
        userService.profileUpdate(profileUpdate);
        // Implementation for updating user profile goes here

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/mybookings")

    public ResponseEntity<List<BookingDto>> getUserBookings()
    {
        // Implementation for fetching user bookings goes here

        return ResponseEntity.ok(userService.getMyBookings());
    }

}
