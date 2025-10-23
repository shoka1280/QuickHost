package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.RoomDto;
import java.util.*;
public interface RoomService {
    RoomDto createRoom(Long Hotelid,RoomDto room);
    RoomDto getRoomById(Long id);
    List<RoomDto> getAllRoomsHotel(Long id);
    RoomDto updateRoomById( Long roomId, RoomDto room);
    void deleteRoomById(Long id);

}
