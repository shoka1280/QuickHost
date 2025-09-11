package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Strategy.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//using all strategy proerly
@Service
public class PricingService {
    //calcuting dyaming pricing for particualr inventory
    public BigDecimal calculateDynamicPricing(Inventory inventory)
    {
        PricingStrategy pricingStrategy=new BasePricingStrategy();
        //decoratory design patter
        //apply additional Strategy
        //everytime we are creating new object everytime
        pricingStrategy=new SurgePricingStratergy(pricingStrategy);
        pricingStrategy=new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy=new UrgencyPricingStratergy(pricingStrategy);//in prcatgect we have wrapped all above strategy(order doesnt matter)
        return pricingStrategy.calculatePrice(inventory);

    }
}
