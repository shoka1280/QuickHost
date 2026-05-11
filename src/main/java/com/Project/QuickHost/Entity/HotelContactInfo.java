package com.Project.QuickHost.Entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable// tell jpathese field are stored in owner entity
public class HotelContactInfo {;
    private String address;
    private String phoneNumber;
    private String email;
    private String location;

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//    public String getPhoneNumber()
//    {
//        return phoneNumber;
//    }
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
}
