package com.Project.QuickHost.Config;


import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    public StripeConfig(@Value("${stripe.secret.key}") String stripe_secret)
    {
        Stripe.apiKey=stripe_secret;
    }
}
