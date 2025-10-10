package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Dto.HotelInfoDto;
import com.Project.QuickHost.Dto.RoomDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.Repository.RoomRepo;
import com.Project.QuickHost.exception.ResourceNotFoundException;
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
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements  HotelService{
    private final HotelRepo hotelRepo;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepo roomRepo;



    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new Hotel with name :{}",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);//intially hotel is inactive
        hotelRepo.save(hotel);

        //current user info
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        log.info("Created new Hotel with id:{}",hotel.getId());
       return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel by id :{}",id);
        Hotel hotel=hotelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        log.info("Fetching Hotel with id :{}",id);

        //if give info to owner of hotel only
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+id);
        }

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto  updateHotelById(Long id,HotelDto hotel) {
        Hotel findHotel=hotelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        //if give info to owner of hotel only
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(findHotel.getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+id);
        }

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
        //if give info to owner of hotel only
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+id);
        }

        log.info("delelting hoted with id:{} ",id);

         //delete the  inventory
        for(Room room:hotel.getRoom()){inventoryService.deleteInventory(room);roomRepo.deleteById(room.getId());}
//        for(Room room:hotel.getRoom()){}
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
        //if give info to owner of hotel only
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Not the current owner of hotel with id "+id);
        }

        log.info("Activating hotel with id : {}",id);
        hotel.setActive(true);
        for(Room room:hotel.getRoom())
        {
            inventoryService.initaliszeRoomForAYear(room);
        }
        hotelRepo.save(hotel);

    }


    @Override
    public List<HotelDto> getAllHotel() {
        List<Hotel>hotels=hotelRepo.findAll();
        if(hotels.isEmpty())
        {
            throw  new ResourceNotFoundException("no hotels found");
        }
        return hotels.stream().map(hotel->modelMapper.map(hotel,HotelDto.class)).collect(Collectors.toList());
    }

    //A public method using hotel browing
    @Override
    public HotelInfoDto getHotelInfoById(Long id)
    {
        Hotel hotel=hotelRepo
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found "));
        List<RoomDto>rooms=hotel.getRoom()
                .stream()
                .map(room->modelMapper.map(room,RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);


    }
}
