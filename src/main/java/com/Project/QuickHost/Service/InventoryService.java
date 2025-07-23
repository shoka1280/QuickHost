package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Room;

public interface InventoryService {
    void initaliszeRoomForAYear(Room room);
    void deleteFutureInventory(Room room);
}
