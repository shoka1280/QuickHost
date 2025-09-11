package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Strategy.BasePricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@RequiredArgsConstructor

public class OccupancyPricingStrategy implements PricingStrategy {
//    @Qualifier("basePricingStrategy")
    private final PricingStrategy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        //getting cyrrent price  of room
        BigDecimal price=wrapped.calculatePrice(inventory);
        //if occupnacy rate is greater than 80% multiplying by 1.2
        double occupancyRate=(double)inventory.getBookedCount()/inventory.getTotalCount();
        if(occupancyRate>0.8)
        {
            price=price.multiply(BigDecimal.valueOf(1.2));
        }

        return price;
    }
}
