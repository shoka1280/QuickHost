package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final BookingService bookingService;

    @PostMapping("/payment")//sigNaute header to checker only stripeis calling
    public ResponseEntity<Void> capturePayments(@RequestBody String payLoad , @RequestHeader("Stripe-Signature") String signHeader)
    {
        try{
            Event event=Webhook.constructEvent(payLoad,signHeader,endpointSecret);
            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

    }
}
