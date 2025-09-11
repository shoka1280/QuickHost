package com.Project.QuickHost.Strategy;

import com.Project.QuickHost.Entity.Inventory;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();//base price avaible in room of inventory

    }
}
