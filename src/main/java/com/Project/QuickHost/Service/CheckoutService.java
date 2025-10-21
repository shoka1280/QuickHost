package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Bookings;


public interface CheckoutService {
    String getCheckOutSession(Bookings booking, String SuccessUrl, String failureUrl);


}
