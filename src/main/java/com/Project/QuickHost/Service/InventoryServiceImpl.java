package com.Project.QuickHost.Service;

import com.Project.QuickHost.Dto.HotelPriceDto;
import com.Project.QuickHost.Dto.HotelSearchRequest;
import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Repository.HotelMinPriceRepo;
import com.Project.QuickHost.Repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inRepo;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepo hotelMinPriceRepo;



    @Override
    public void initaliszeRoomForAYear(Room room) {//used when creating a new room n activting hotel
        log.info("Initializing inventory for room: {}", room.getId());
        LocalDate today=LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for(;!today.isAfter(endDate);today=today.plusDays(1)) {
        Inventory inventory=Inventory.builder()
                .hotel(room.getHotel())
                .room(room)
                .totalCount(room.getTotalCount())
                .price(room.getBasePrice())
                .date(today)
                .city(room.getHotel().getCity())
                .bookedCount(0)
                .surgeFactor(BigDecimal.ONE)
                .closed(false)
                .createdAt(LocalDate.now())
                .reservedCount(0)//by default when room is created reserved count is 0
                .build();

         inRepo.save(inventory);

    }
    }

    @Override
    public void deleteInventory(Room room) {
//        LocalDate today=LocalDate.now();
        log.info("Deleting inventory for room:{}",room.getId());
        inRepo.deleteByRoom(room);//aaj ke badh jitne bhi hai,room delete krdo


    }

    @Override
    public Page<HotelPriceDto> searchHotel(HotelSearchRequest req) {
        log.info("Searching hotels with for city {} from {} to {}", req.getCity(), req.getStartDate(), req.getEndDate());
        Pageable pageable= PageRequest.of(req.getPage(), req.getSize());
        log.info("Searching hotels with request: {}", req);
        //note we need to get all availabe(totalCount-bookedCount>=roomsCounts) hotel available onstart and endDate for particular city,roomcount
        //criteria of inventory i startDate<=date<=endDate ,city
        //we have created inventory to denormilize data[no joins]
        //group the response based on rooms
        //get distinct hotel
        int dateCount=(int) ChronoUnit.DAYS.between(req.getStartDate(),req.getEndDate())+1;
        //business logic-90days(ifelsecondition)
//        Page<Hotel> hotelPage=inRepo.findHotelsWithAvailableInventory(req.getCity(),req.getStartDate(),req.getEndDate(),req.getRoomsCount(),dateCount,pageable);
        Page<HotelPriceDto>Hotelpage=hotelMinPriceRepo.findHotelsWithAvailableInventory(req.getCity(),req.getStartDate(),req.getEndDate(),pageable);

//        return page.map(ho->modelMapper.map(ho, HotelMinPrice.class));//use have map method dont need stream
         return Hotelpage;
    }
}
