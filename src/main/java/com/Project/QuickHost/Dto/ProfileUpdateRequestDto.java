package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.enums.Gender;
import lombok.Data;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private String dateOfBirth;

    private Gender gender;
    private String DOB;
}
