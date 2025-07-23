package com.Project.QuickHost.Service;

import com.Project.QuickHost.Entity.Inventory;
import com.Project.QuickHost.Entity.Room;
import com.Project.QuickHost.Repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inRepo;


    @Override
    public void initaliszeRoomForAYear(Room room) {//used when creating a new room n activting hotel
        log.info("Initializing inventory for room: {}", room.getId());
        LocalDate today=LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for(;!today.isAfter(endDate);today=today.plusDays(1)) {
        Inventory inventory=Inventory.builder()
                .hotel(room.getHotel())
                .room(room)
                .totalCount(room.getTotalcount())
                .price(room.getBasePrice())
                .date(today)
                .city(room.getHotel().getCity())
                .bookedCount(0)
                .surgeFactor(BigDecimal.ONE)
                .closed(false)
                .createdAt(LocalDate.now())
                .build();

         inRepo.save(inventory);

    }
    }

    @Override
    public void deleteFutureInventory(Room room) {
        LocalDate today=LocalDate.now();
        inRepo.deleteByDateAfterAndRoom(today,room);//aaj ke badh jitne bhi hai,room delete krdo


    }
}
