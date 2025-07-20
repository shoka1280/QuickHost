package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;//spring will automaticall find is implementation nd inject
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id)
    {
        log.info("fetching hotel by id: {}", id);
        return new ResponseEntity<>(hotelService.getHotelById(id),HttpStatus.FOUND);
    }
    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotel)
    {
        log.info("Attepting hotel with name: {}",hotel.getName());
        return new ResponseEntity<>(hotelService.createNewHotel(hotel), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id,@RequestBody HotelDto hotel)
    {
        return new ResponseEntity<>(hotelService.updateHotelById(id,hotel),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public boolean deleteHoteById(@PathVariable Long id)
    {
        log.info("deleting hotel with id: {}",id);
        return hotelService.deleteHotelById(id);
    }





}
