package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.RoomDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.RoomRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final ModelMapper modelMapper;
    private final HotelRepo hotelRepo;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createRoom(Long Hotelid,RoomDto roomDto) {
        log.info("Creating new Room with Hotelid: {}",Hotelid);
        Hotel hotel=hotelRepo.findById(Hotelid).orElseThrow(()->new RuntimeException("Hotel not found iwht id {}"+Hotelid));
        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);

        Room createRoom=roomRepo.save(room);
        if(hotel.isActive()){
            inventoryService.initaliszeRoomForAYear(room);

        }
        return modelMapper.map(createRoom,RoomDto.class);//response

    }

    @Override
    public RoomDto getRoomById(Long id) {

        Room room=roomRepo.findById(id).orElseThrow(()->new RuntimeException("Room not found with id: {}"+id));
        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsHotel(Long Hotelid) {
        log.info("getting rooms with hotel id:{}",Hotelid);
        Hotel hotel=hotelRepo.findById(Hotelid).orElseThrow(()->new RuntimeException("Hotel not found iwht id {}"+Hotelid));
        List<Room> room=hotel.getRoom();
        return room.stream()
                .map(room1->modelMapper.map(room1,RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto updateRoomById(Long id, RoomDto room) {
        Room room1=roomRepo.findById(id).orElseThrow(()->new RuntimeException("Room not found with id: {}"+id));
        log.info("udating room with id:{} ",id);
        modelMapper.map(room,room1);
        room1.setId(id);
        Room updatedRoom=roomRepo.save(room1);
        return modelMapper.map(updatedRoom,RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {
        Room room=roomRepo.findById(id).orElseThrow(()->new RuntimeException("Room not found with id: {}"+id));
        log.info("deleting room with id:{}",id);
        inventoryService.deleteFutureInventory(room);

        roomRepo.deleteById(id);
        //TODO:delete all inventory of this room

    }
}
