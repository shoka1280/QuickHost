package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.RoomDto;
import com.Project.QuickHost.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels/{hotelId}/rooms")
public class RoomController {//Roomsa dmin controller
    private final RoomService roomService;
    @GetMapping("/{id}")
        public ResponseEntity<RoomDto> getRoomByid(@PathVariable Long id)
        {
            RoomDto room=roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        }
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsOfHotel(@PathVariable(name="hotelId") Long id)
    {
        List<RoomDto> rooms=roomService.getAllRoomsHotel(id);
        return ResponseEntity.ok(rooms);
    }
    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable(name="hotelId")Long id,@RequestBody RoomDto roomDto)
    {
        RoomDto room=roomService.createRoom(id,roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<RoomDto>updateRoomById(@PathVariable(name="hotelId")Long Hotelid,@PathVariable Long id,@RequestBody RoomDto room)
    {
        RoomDto updatedRoom=roomService.updateRoomById(id, room);
        return ResponseEntity.ok(updatedRoom);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomByid(@PathVariable(name="hotelId")Long Hotelid,@PathVariable Long id)
    {
        roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }





}
