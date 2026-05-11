package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.*;
import com.Project.QuickHost.Entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initaliszeRoomForAYear(Room room);
    void deleteInventory(Room room);

    Page<HotelPriceResponseDto> searchHotel(HotelSearchRequest req);

    List<InventoryDto> getInventoryOfRoom(Long roomId);

    void UpdateInventoryOfRoom(Long roomId, UpdateInventoryRequestDto updateInvDto);
}
