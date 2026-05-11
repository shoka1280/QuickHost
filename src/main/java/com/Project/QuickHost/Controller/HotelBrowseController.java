package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.*;
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
   public ResponseEntity< PageModel<HotelPriceResponseDto>>serchingHotel(@RequestBody HotelSearchRequest req)
    {
        //for faciliting search we will use inventory service;
      Page<HotelPriceResponseDto> page= invService.searchHotel(req);
        PageModel<HotelPriceResponseDto> response = new PageModel<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
    {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
