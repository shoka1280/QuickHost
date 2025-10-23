package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.BookingDto;
import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Dto.HotelInfoDto;
import com.Project.QuickHost.Dto.ReportDto;


import java.time.LocalDate;
import java.util.List;

public interface HotelService {
      //define methods only hair
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id,HotelDto hotel);
   boolean deleteHotelById(Long id);
   void activateHotel(Long id);
    List<HotelDto> getAllHotel();

    HotelInfoDto getHotelInfoById(Long id);

    List<BookingDto> getAllBooking(Long hotelId);

    ReportDto getReport(Long hotelId, LocalDate startDate,LocalDate endDate);
}