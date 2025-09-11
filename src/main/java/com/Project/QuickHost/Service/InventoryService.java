package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelPriceDto;
import com.Project.QuickHost.Dto.HotelSearchRequest;
import com.Project.QuickHost.Entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initaliszeRoomForAYear(Room room);
    void deleteInventory(Room room);

    Page<HotelPriceDto> searchHotel(HotelSearchRequest req);
}
