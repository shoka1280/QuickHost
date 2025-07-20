package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Repository.HotelRepo;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.Project.QuickHost.Config.MapperConfig;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements  HotelService{
    private final HotelRepo hotelRepo;
    private final ModelMapper modelMapper;



    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new Hotel with name :",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);//intially hotel is inactive
        hotelRepo.save(hotel);
        log.info("Created new Hotel with id:",hotel.getId());
       return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel by id :",id);
        Hotel hotel=hotelRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        log.info("Fetching Hotel with id :",id);
//        if(!hotel.isActive()){
//            throw new RuntimeException("Hotel is not active");
//
//        }
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
    public boolean deleteHotelById(Long id) {
        Hotel hotel=hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("didnot found hotel"));
        log.info("delelting hoted with id: ",id);
         hotelRepo.deleteById(id);
         //todo:delete the future invetort
        return true;
    }
}
