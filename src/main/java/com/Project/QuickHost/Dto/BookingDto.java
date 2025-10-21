package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;
//    private Hotel hotel;
//    private Room room;
    private Integer roomCount; // number of rooms booked for this booking
//    private User user;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

//    private Payement payement;


    private BookingStatus bookingStatus;


    private Set<GuestDto> guests;
    private BigDecimal price;

}
