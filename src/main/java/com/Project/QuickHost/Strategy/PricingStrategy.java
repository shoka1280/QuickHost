package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
@Service
public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
