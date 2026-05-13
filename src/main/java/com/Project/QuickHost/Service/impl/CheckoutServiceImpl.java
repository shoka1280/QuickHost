package com.Project.QuickHost.Service.impl;

import com.Project.QuickHost.Entity.Bookings;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Repository.BookingRepo;
import com.Project.QuickHost.Service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {
    private final BookingRepo bookrepo;

    @Override
    public String getCheckOutSession(Bookings booking, String successUrl, String failureUrl) {
        log.info("Create session for booking id: {} ",booking.getId());
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//Current user
        try{
            CustomerCreateParams customerCreateParams=CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            Customer customer=Customer.create(customerCreateParams//we neeed Customer greate params to create customer

            );
            SessionCreateParams Sessionparams=SessionCreateParams.builder()

                    .setMode(SessionCreateParams.Mode.PAYMENT)//session create params to create session
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(//how stripe check out look like
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)//booking 1 hotel at time

                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())//converting to paisa,lowest possible value
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName()+" : "+booking.getRoom().getType())
                                                                    .setDescription("Booking id :"+booking.getId())
                                                                    .build()//can add image as well

                                                    ).build()
                                    ).build()

                    )//notice its to show which product is added[check stripe docs]
                    .build();

            Session session=Session.create(Sessionparams);//Com.model.stripe.checkout

      booking.setStripePaymentsessionId(session.getId());
      bookrepo.save(booking);
      log.info("Session created successfully for bookig id: {} ",booking.getId());
      return  session.getUrl();

        }catch (StripeException e)
        {
            throw new RuntimeException(e);
        }





    }
}
