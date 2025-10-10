package com.Project.QuickHost.Security.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {

   private  String name;
   @Email(message = "hello it should be email")
    private String email;
   @Size(min =6,message = "password should be at least 6 character",max=20)
    private String password;

}
