package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@RequiredArgsConstructor
@Slf4j
public class UrgencyPricingStratergy implements PricingStrategy{
    private final PricingStrategy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        log.info("Urgency prcicing");
        //calculating current Price;
        BigDecimal price=wrapped.calculatePrice(inventory);
        LocalDate today=LocalDate.now();
        //if before seven days;
        if(!inventory.getDate().isBefore(today)&&inventory.getDate().isBefore(today.plusDays(7)))
        {
            price=price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;


    }
}
