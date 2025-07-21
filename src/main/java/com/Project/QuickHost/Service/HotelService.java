package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Entity.Hotel;

public interface HotelService {
      //define methods only hair
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id,HotelDto hotel);
   boolean deleteHotelById(Long id);
   void activateHotel(Long id);

}