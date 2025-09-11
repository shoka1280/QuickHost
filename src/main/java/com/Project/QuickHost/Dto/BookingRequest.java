package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BookingRequest {
    private Long hotelId;
    private Long  roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer roomCount;


}
