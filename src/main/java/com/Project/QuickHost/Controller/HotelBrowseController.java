package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.HotelDto;
import com.Project.QuickHost.Dto.HotelInfoDto;
import com.Project.QuickHost.Dto.HotelPriceDto;
import com.Project.QuickHost.Dto.HotelSearchRequest;
import com.Project.QuickHost.Service.HotelService;
import com.Project.QuickHost.Service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor

public class HotelBrowseController {
    private final InventoryService invService;
    private final HotelService hotelService;
    @GetMapping("/search")
    private ResponseEntity<Page<HotelPriceDto>>serchingHotel(@RequestBody HotelSearchRequest req)
    {
        //for faciliting search we will use inventory service;
      Page<HotelPriceDto> page= invService.searchHotel(req);
      return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
    {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
