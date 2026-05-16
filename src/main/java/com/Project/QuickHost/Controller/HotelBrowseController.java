package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.*;
import com.Project.QuickHost.Entity.Review;
import com.Project.QuickHost.Service.HotelService;
import com.Project.QuickHost.Service.InventoryService;

import com.Project.QuickHost.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor

public class HotelBrowseController {
    private final InventoryService invService;
    private final HotelService hotelService;
    public final ReviewService reviewService;
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

    @GetMapping("/hotels/{hotelId}/reviews")
    public ResponseEntity<PageModel<ReviewResponse>> list(
            @PathVariable Long hotelId,
            @PageableDefault(size = 20, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Review> page = reviewService.getHotelReviews(hotelId, pageable);
        List<ReviewResponse> content = page.getContent().stream().map(ReviewResponse::from).toList();
        PageModel<ReviewResponse> body = new PageModel<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());

        return ResponseEntity.ok(body);
    }

}
