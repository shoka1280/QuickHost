package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    // Additional query methods can be defined here if needed
}
