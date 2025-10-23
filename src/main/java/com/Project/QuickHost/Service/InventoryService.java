package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelPriceDto;
import com.Project.QuickHost.Dto.HotelSearchRequest;
import com.Project.QuickHost.Dto.InventoryDto;
import com.Project.QuickHost.Dto.UpdateInventoryRequestDto;
import com.Project.QuickHost.Entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initaliszeRoomForAYear(Room room);
    void deleteInventory(Room room);

    Page<HotelPriceDto> searchHotel(HotelSearchRequest req);

    List<InventoryDto> getInventoryOfRoom(Long roomId);

    List<InventoryDto> UpdateInventoryOfRoom(Long roomId, UpdateInventoryRequestDto updateInvDto);
}
