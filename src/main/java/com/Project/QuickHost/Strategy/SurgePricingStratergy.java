package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@RequiredArgsConstructor
public class SurgePricingStratergy implements PricingStrategy{
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgeFactor());//multiply the inventory surge factor multiplying with current factor
    }
}
