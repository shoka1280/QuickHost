package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.HotelContactInfo;

import lombok.Data;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String city;
   //storing url in array of text
    private String[] photos;

    private String[] amenities;

   //contactInfo will be embedded in hotel table(same)
    private HotelContactInfo contactInfo;//will embed contact_info_phonenumber,contact_info_address

    private boolean active;
}
