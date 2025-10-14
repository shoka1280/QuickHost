package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Bookings;
import org.springframework.stereotype.Service;


public interface CheckoutService {
    String getCheckOutSession(Bookings booking, String SuccessUrl, String failureUrl);


}
