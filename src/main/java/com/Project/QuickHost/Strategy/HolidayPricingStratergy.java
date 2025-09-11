package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStratergy implements PricingStrategy {
    private final PricingStrategy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isTodayHoliday = true;//TODO:Call an APi
        if (isTodayHoliday) {
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}