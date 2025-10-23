package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.RoomDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.RoomRepo;
import com.Project.QuickHost.exception.UnAuthorisedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final ModelMapper modelMapper;
    private final HotelRepo hotelRepo;
    private final InventoryService inventoryService;

    @Override

    public RoomDto createRoom(Long Hotelid,RoomDto roomDto) {
        log.info("Creating new Room with Hotelid: {}",Hotelid);
        Hotel hotel=hotelRepo.findById(Hotelid).orElseThrow(()->new RuntimeException("Hotel not found iwht id {}"+Hotelid));

        //if give info to owner of hotel only
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user1=hotel.getOwner();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+hotel.getId());
        }

        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);

        Room createRoom=roomRepo.save(room);
        if(hotel.isActive()){
            inventoryService.initaliszeRoomForAYear(createRoom);

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
    @Transactional
    public RoomDto updateRoomById(Long id,  RoomDto room) {
        Room room1=roomRepo.findById(id).orElseThrow(()->new RuntimeException("Room not found with id: {}"+id));
        //check if ur owner or not
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room1.getHotel().getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+room1.getHotel().getId());
        }
        log.info("udating room with id:{} ",id);
        modelMapper.map(room,room1);
        room1.setId(id);
        Room updatedRoom=roomRepo.save(room1);
        return modelMapper.map(updatedRoom,RoomDto.class);
        //TODO: to update price adn size update inventory
    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {
        Room room=roomRepo.findById(id).orElseThrow(()->new RuntimeException("Room not found with id: {}"+id));

        //check if ur owner or not
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+room.getHotel().getId());
        }

        log.info("deleting room with id:{}",id);
        inventoryService.deleteInventory(room);

        roomRepo.deleteById(id);
        //TODO:delete all inventory of this room

    }
}
