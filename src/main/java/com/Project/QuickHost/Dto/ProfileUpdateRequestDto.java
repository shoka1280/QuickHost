package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProfileUpdateRequestDto {
    private String name;
    private String dateOfBirth;
    private String phonenumber;
    private Gender gender;
}
