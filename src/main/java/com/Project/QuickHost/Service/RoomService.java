package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Dto.RoomDto;
import java.util.*;
public interface RoomService {
    RoomDto createRoom(Long Hotelid,RoomDto room);
    RoomDto getRoomById(Long id);
    List<RoomDto> getAllRoomsHotel(Long id);
    RoomDto updateRoomById(Long id, RoomDto room);
    void deleteRoomById(Long id);

}
