package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements  HotelService{
    private final HotelRepo hotelRepo;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;



    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new Hotel with name :{}",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);//intially hotel is inactive
        hotelRepo.save(hotel);
        log.info("Created new Hotel with id:{}",hotel.getId());
       return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel by id :{}",id);
        Hotel hotel=hotelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        log.info("Fetching Hotel with id :{}",id);

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto  updateHotelById(Long id,HotelDto hotel) {
        Hotel findHotel=hotelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        log.info("udating hotel with id: {}", id);
        modelMapper.map(hotel,findHotel);
        findHotel.setId(id);

        Hotel updateHotel=hotelRepo.save(findHotel);
        return modelMapper.map(updateHotel,HotelDto.class);
    }

    @Override
    @Transactional//used when w more than one table involved,if one fails everything will got back to prev stage
    public boolean deleteHotelById(Long id) {
        Hotel hotel=hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("didnot found hotel"));
        log.info("delelting hoted with id:{} ",id);

         //todo:delete the future inventory
        for(Room room:hotel.getRoom()){inventoryService.deleteFutureInventory(room);}
        hotelRepo.deleteById(id);

        return true;
    }

    @Override
    @Transactional//used when w more than one table involved,if one fails everything will got back to prev stage
    public void activateHotel(Long id) {
        log.info("Atempting to activate hotel with id: {}",id);
        Hotel hotel=hotelRepo
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found "));
        log.info("Activating hotel with id : {}",id);
        hotel.setActive(true);
        for(Room room:hotel.getRoom())
        {
            inventoryService.initaliszeRoomForAYear(room);
        }
        hotelRepo.save(hotel);

    }
}
