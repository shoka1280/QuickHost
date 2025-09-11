package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.Gender;
import com.Project.QuickHost.Entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
public class GuestDto {
    private Long id;


    private User user;

    private String name;


    private Gender gender;

    private Integer age;


//    private Set<Bookings> bookings;
}
