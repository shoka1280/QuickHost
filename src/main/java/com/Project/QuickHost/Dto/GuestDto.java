package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.enums.Gender;
import com.Project.QuickHost.Entity.User;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;


    private User user;

    private String name;


    private Gender gender;

    private Integer age;


//    private Set<Bookings> bookings;
}
