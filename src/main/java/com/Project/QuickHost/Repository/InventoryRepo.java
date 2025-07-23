package com.Project.QuickHost.Repository;

import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    void deleteByDateAfterAndRoom(LocalDate date,Room room );
    // Additional query methods can be defined here if needed
}
